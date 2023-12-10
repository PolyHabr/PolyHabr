package com.polyhabr.poly_back.integration

import com.icegreen.greenmail.configuration.GreenMailConfiguration
import com.icegreen.greenmail.junit5.GreenMailExtension
import com.icegreen.greenmail.util.GreenMailUtil
import com.icegreen.greenmail.util.ServerSetupTest
import com.polyhabr.poly_back.config.DataLoader
import com.polyhabr.poly_back.controller.auth.*
import com.polyhabr.poly_back.repository.auth.UsersRepository
import org.junit.FixMethodOrder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension
import org.junit.runners.MethodSorters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.http.client.ClientHttpResponse
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.client.DefaultResponseErrorHandler
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException


@Testcontainers
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ActiveProfiles("test")
class UserIT @Autowired constructor(
    private final val testRestTemplate: TestRestTemplate,
    private final val dataLoader: DataLoader,
    private val usersRepository: UsersRepository,
    private val authController: AuthController
) {
    companion object {
        @Container
        val container = PostgreSQLContainer<Nothing>("postgres:12").apply {
            withDatabaseName("postgres")
            withUsername("postgres")
            withPassword("postgres")
        }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", container::getJdbcUrl);
            registry.add("spring.datasource.password", container::getPassword);
            registry.add("spring.datasource.username", container::getUsername);
        }

        @JvmField
        @RegisterExtension
        val greenMail = GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("duke", "springboot"))
    }

    init {
        testRestTemplate.restTemplate.requestFactory = HttpComponentsClientHttpRequestFactory()
        testRestTemplate.restTemplate.errorHandler = object : DefaultResponseErrorHandler() {
            @Throws(IOException::class)
            override fun hasError(response: ClientHttpResponse): Boolean {
                val statusCode: HttpStatus = response.statusCode
                return statusCode.series() === HttpStatus.Series.SERVER_ERROR
            }
        }
    }

    private val objectMapper = ObjectMapper()

    private fun loginAndGetToken(username: String, password: String): String {
        val loginUser = LoginUser(username, password)
        val loginResponseRaw =
            testRestTemplate.postForObject("/api/auth/signin", HttpEntity(loginUser, null), String::class.java)
        val loginResponse = objectMapper.readTree(loginResponseRaw)
        val token = loginResponse.get("accessToken")
        return token.textValue()
    }

    private fun loginAdminUser() = loginAndGetToken("admin", "admincool")
    private fun loginDefaultUser() = loginAndGetToken("uservasya", "userbroke")
    private fun createDefaultHeaders(token: String? = null) =
        HttpHeaders().apply {
            accept = listOf(MediaType.APPLICATION_JSON)
            add("Content-Type", "application/json")
            token?.let {
                add("Authorization", "Bearer $it")
            }
        }

    @BeforeEach
    fun setup() {
        dataLoader.clean()
    }

    @Test
    fun `register user`() {
        dataLoader.createDefaultRole()

        val newUser = NewUser(
            username = "uservanya",
            firstName = "firstName",
            lastName = "lastName",
            email = "uservan12312ya@notfake.com",
            password = "a12345678"
        )

        val response1 = testRestTemplate.postForEntity(
            "/api/auth/signup",
            HttpEntity(newUser, createDefaultHeaders()),
            ResponseMessage::class.java
        )

        Assertions.assertNotNull(response1.body)
        Assertions.assertEquals(HttpStatus.OK, response1.statusCode)

        val message = greenMail.receivedMessages?.get(0)
        Assertions.assertNotNull(message)

        val messageBody = GreenMailUtil.getBody(message)
        val token = messageBody
            .substringAfter("api/auth/verify?code=3D")
            .substringBefore('"')
            .filterNot { it == '\r' }
            .filterNot { it == '\n' }
            .filterNot { it == '=' }

        Assertions.assertTrue(token.isNotEmpty())

        val userFromDB = usersRepository.findByLogin(newUser.username)
        val tokenFromDB = userFromDB?.verificationCode

        Assertions.assertNotNull(userFromDB)
        Assertions.assertNotNull(tokenFromDB)
        Assertions.assertEquals(tokenFromDB, token)

        val response2 = testRestTemplate.getForEntity(
            "/api/auth/verify?code=${token}",
            String::class.java
        )

        Assertions.assertEquals(HttpStatus.OK, response2.statusCode)

        val userFromDBAfterVerify = usersRepository.findByLogin(newUser.username)

        Assertions.assertNotNull(userFromDBAfterVerify)
        Assertions.assertNull(userFromDBAfterVerify!!.verificationCode)
    }

    @Test
    fun `forgot password`() {
        val role = dataLoader.createDefaultRole()
        val user = dataLoader.createUserDefault(role)
        val newPassword = "a9999999999a"

        val response1 = testRestTemplate.postForEntity(
            "/api/auth/forgotPassword?email=${user.email}",
            HttpEntity(null, createDefaultHeaders()),
            Unit::class.java
        )

        Assertions.assertEquals(HttpStatus.OK, response1.statusCode)

        val message = greenMail.receivedMessages?.get(0)
        Assertions.assertNotNull(message)

        val messageBody = GreenMailUtil.getBody(message)
        val token = messageBody
            .substringAfter("api/auth/changePassword?token=3D")
            .substringBefore('"')
            .filterNot { it == '\r' }
            .filterNot { it == '\n' }
            .filterNot { it == '=' }

        Assertions.assertTrue(token.isNotEmpty())

        val userFromDB = usersRepository.findByLogin(user.login)
        val tokenFromDB = userFromDB?.resetToken

        Assertions.assertNotNull(userFromDB)
        Assertions.assertNotNull(tokenFromDB)
        Assertions.assertEquals(tokenFromDB, token)

        val passwordChange = PasswordChange(token = token, newPassword = newPassword)
        val response2 = testRestTemplate.exchange(
            "/api/auth/savePassword",
            HttpMethod.PUT,
            HttpEntity(passwordChange, createDefaultHeaders()),
            Unit::class.java
        )

        Assertions.assertEquals(HttpStatus.OK, response2.statusCode)

        val accessTokenAfterChangePassword = loginAndGetToken(user.login, newPassword)

        Assertions.assertTrue(accessTokenAfterChangePassword.isNotEmpty())
    }

    @Test
    fun `check free login success`() {
        val role = dataLoader.createDefaultRole()
        val user = dataLoader.createUserDefault(role)
        val newLogin = user.login + "123"

        val response = testRestTemplate.getForEntity(
            "/api/auth/checkFreeLogin?login=${newLogin}",
            Unit::class.java
        )

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)

        val userFromDB = usersRepository.findByLogin(newLogin)
        Assertions.assertNull(userFromDB)
    }

    @Test
    fun `check free login failure`() {
        val role = dataLoader.createDefaultRole()
        val user = dataLoader.createUserDefault(role)

        val response = testRestTemplate.getForEntity(
            "/api/auth/checkFreeLogin?login=${user.login}",
            Unit::class.java
        )

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)

        val userFromDB = usersRepository.findByLogin(user.login)
        Assertions.assertNotNull(userFromDB)
    }
}