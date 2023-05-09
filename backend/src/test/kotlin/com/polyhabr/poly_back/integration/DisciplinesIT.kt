package com.polyhabr.poly_back.integration

import com.polyhabr.poly_back.config.DataLoader
import com.polyhabr.poly_back.controller.auth.LoginUser
import com.polyhabr.poly_back.controller.model.disciplineType.request.DisciplineTypeRequest
import com.polyhabr.poly_back.controller.model.disciplineType.response.DisciplineTypeCreateResponse
import com.polyhabr.poly_back.controller.model.disciplineType.response.DisciplineTypeListResponse
import com.polyhabr.poly_back.entity.auth.User
import com.polyhabr.poly_back.repository.DisciplineTypeRepository
import com.polyhabr.poly_back.repository.auth.UsersRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.*
import org.springframework.http.client.ClientHttpResponse
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.security.crypto.password.PasswordEncoder
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
class DisciplinesIT @Autowired constructor(
    private final val testRestTemplate: TestRestTemplate,
    private final val dataLoader: DataLoader,
    val disciplineTypeRepository: DisciplineTypeRepository,
    val usersRepository: UsersRepository,
    val encoder: PasswordEncoder
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
    private fun loginDefaultUser() = loginAndGetToken("uservasya", "a12345678")
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
    fun `login under the admin and create discipline is success`() {
        val adminRole = dataLoader.createRoleAdmin()
        dataLoader.createUserAdmin(adminRole)

        val token = loginAdminUser()
        Assertions.assertTrue(token.isNotEmpty())

        val disciplineTypeRequest = DisciplineTypeRequest("newDiscipline")
        val response = testRestTemplate.postForEntity(
            "/discipline_type/create",
            HttpEntity(disciplineTypeRequest, createDefaultHeaders(token)),
            DisciplineTypeCreateResponse::class.java
        )

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        Assertions.assertNotNull(response.body)
        Assertions.assertTrue(response.body!!.isSuccess)

        val createdDisciplineId = response.body!!.id!!
        val checkCreatedDisciplineFromRepository = disciplineTypeRepository.findByIdOrNull(createdDisciplineId)

        Assertions.assertNotNull(checkCreatedDisciplineFromRepository)
        Assertions.assertEquals(createdDisciplineId, checkCreatedDisciplineFromRepository!!.id)
        Assertions.assertEquals(disciplineTypeRequest.name, checkCreatedDisciplineFromRepository.name)
    }

    @Test
    fun `create discipline without login is failure 401`() {
        val disciplineTypeRequest = DisciplineTypeRequest("newDiscipline")
        val response = testRestTemplate.postForEntity(
            "/discipline_type/create",
            HttpEntity(disciplineTypeRequest, createDefaultHeaders()),
            DisciplineTypeCreateResponse::class.java
        )
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)

        val savedDiscipline = disciplineTypeRepository.findByName(disciplineTypeRequest.name!!)
        Assertions.assertNull(savedDiscipline)
    }

    @Test
    fun `create discipline with default user login is failure 403`() {
        val defaultRole = dataLoader.createDefaultRole()
        val defaultUSer = usersRepository.save(
            User(
                login = "uservanya",
                password = encoder.encode("a12345678"),
                name = "vanya",
                surname = "ivanov",
                email = "uservanya@notfake.com",
                enabled = true,
                roles = listOf(defaultRole)
            )
        )

        val token = loginAndGetToken("uservanya", "a12345678")
        Assertions.assertTrue(token.isNotEmpty())

        val disciplineTypeRequest = DisciplineTypeRequest("newDiscipline")
        val response = testRestTemplate.postForEntity(
            "/discipline_type/create",
            HttpEntity(disciplineTypeRequest, createDefaultHeaders(token)),
            DisciplineTypeCreateResponse::class.java
        )
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.statusCode)

        val savedDiscipline = disciplineTypeRepository.findByName(disciplineTypeRequest.name!!)
        Assertions.assertNull(savedDiscipline)
    }

    @Test
    fun `get disciplines`() {
        val disciplines = dataLoader.createList6Disciplines()

        val offset = 0
        val size = 6
        val response = testRestTemplate.getForEntity(
            "/discipline_type?offset=${offset}&size=${size}",
            DisciplineTypeListResponse::class.java
        )

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        Assertions.assertNotNull(response.body)
        Assertions.assertTrue(response.body!!.contents.size == disciplines.size)

        val names = response.body!!.contents.map { it.name }
        Assertions.assertTrue(names.containsAll(disciplines.map { it.name }))
    }

    @Test
    fun `search for a discipline by name`() {
        val disciplines = dataLoader.createList6Disciplines()
        val randomPick = disciplines.indices.random()

        val offset = 0
        val size = Int.MAX_VALUE
        val response = testRestTemplate.getForEntity(
            "/discipline_type/search?offset=${offset}&size=${size}&prefix=${disciplines[randomPick].name}",
            DisciplineTypeListResponse::class.java
        )

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        Assertions.assertNotNull(response.body)
        Assertions.assertTrue(response.body!!.contents.isNotEmpty())

        val names = response.body!!.contents.map { it.name }
        val found = names.find { it == disciplines[randomPick].name }

        Assertions.assertNotNull(found)
    }

    @Test
    fun `login under admin and deleting discipline by id is success`() {
        val adminRole = dataLoader.createRoleAdmin()
        val adminUser = dataLoader.createUserAdmin(adminRole)
        val disciplines = dataLoader.createList6Disciplines()
        val randomPick = disciplines.indices.random()

        val token = loginAdminUser()
        Assertions.assertTrue(token.isNotEmpty())

        val response = testRestTemplate.exchange(
            "/discipline_type/delete?id=${disciplines[randomPick].id}",
            HttpMethod.DELETE,
            HttpEntity(null, createDefaultHeaders(token)),
            Unit::class.java
        )

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)

        val savedDiscipline = disciplineTypeRepository.findByName(disciplines[randomPick].name!!)
        Assertions.assertNull(savedDiscipline)
    }

    @Test
    fun `deleting discipline by id without login is failure 401`() {
        val disciplines = dataLoader.createList6Disciplines()
        val randomPick = disciplines.indices.random()

        val response = testRestTemplate.exchange(
            "/discipline_type/delete?id=${disciplines[randomPick].id}",
            HttpMethod.DELETE,
            HttpEntity(null, createDefaultHeaders()),
            Unit::class.java
        )

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)

        val savedDiscipline = disciplineTypeRepository.findByName(disciplines[randomPick].name!!)
        Assertions.assertNotNull(savedDiscipline)
    }

    @Test
    fun `deleting discipline by id with default user login login is failure 403`() {
        val defaultRole = dataLoader.createDefaultRole()
        val defaultUSer = usersRepository.save(
            User(
                login = "uservanya",
                password = encoder.encode("a12345678"),
                name = "vanya",
                surname = "ivanov",
                email = "uservanya@notfake.com",
                enabled = true,
                roles = listOf(defaultRole)
            )
        )
        val disciplines = dataLoader.createList6Disciplines()
        val randomPick = disciplines.indices.random()

        val token = loginAndGetToken("uservanya", "a12345678")
        Assertions.assertTrue(token.isNotEmpty())

        val response = testRestTemplate.exchange(
            "/discipline_type/delete?id=${disciplines[randomPick].id}",
            HttpMethod.DELETE,
            HttpEntity(null, createDefaultHeaders(token)),
            Unit::class.java
        )

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.statusCode)

        val savedDiscipline = disciplineTypeRepository.findByName(disciplines[randomPick].name!!)
        Assertions.assertNotNull(savedDiscipline)
    }

}