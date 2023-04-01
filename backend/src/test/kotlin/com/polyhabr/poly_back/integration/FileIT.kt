package com.polyhabr.poly_back.integration

import com.polyhabr.poly_back.config.DataLoader
import com.polyhabr.poly_back.controller.auth.LoginUser
import com.polyhabr.poly_back.entity.File
import com.polyhabr.poly_back.entity.auth.User
import com.polyhabr.poly_back.repository.FileRepository
import com.polyhabr.poly_back.repository.auth.UsersRepository
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.io.ClassPathResource
import org.springframework.http.*
import org.springframework.http.client.ClientHttpResponse
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.http.converter.FormHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.util.FileCopyUtils
import org.springframework.util.MimeTypeUtils
import org.springframework.util.ResourceUtils
import org.springframework.web.client.DefaultResponseErrorHandler
import org.springframework.web.context.WebApplicationContext
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException
import java.nio.file.Files


@Testcontainers
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class FileIT @Autowired constructor(
    private final val testRestTemplate: TestRestTemplate,
    private final val dataLoader: DataLoader,
    val fileRepository: FileRepository,
    val usersRepository: UsersRepository,
    val encoder: PasswordEncoder,
    val context: WebApplicationContext
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
        testRestTemplate.restTemplate.messageConverters.apply {
            add(FormHttpMessageConverter())
            add(MappingJackson2HttpMessageConverter())
        }
    }

    private val objectMapper = ObjectMapper()

    private lateinit var mvc: MockMvc

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

    private fun createMultipartHeaders(token: String? = null) =
        HttpHeaders().apply {
            contentType = MediaType.MULTIPART_FORM_DATA
            token?.let {
                add("Authorization", "Bearer $it")
            }
        }

    @BeforeEach
    fun setup() {
        mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply<DefaultMockMvcBuilder>(springSecurity())
            .build()
        dataLoader.clean()
    }

    @Test
    fun `create file pdf`() {
        val defaultRole = dataLoader.createDefaultRole()
        val defaultUser = usersRepository.save(
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
        val articleTypes = dataLoader.createList6ArticleType()
        val tags = dataLoader.createList9Tags()
        val disciplines = dataLoader.createList6Disciplines()
        val article = dataLoader.createArticle(
            "someTitle",
            "somePreview",
            "text",
            articleType = articleTypes[0],
            user = defaultUser,
            tag = tags[0],
            discipline = disciplines[0]
        )
        val token = loginAndGetToken("uservanya", "a12345678")

        val file = createDummyFile()

        val result = mvc.perform(
            MockMvcRequestBuilders.multipart("/files")
                .file(file)
                .param("articleId", article.id!!.toString())
                .header("Authorization", "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andReturn()
        val parsed = objectMapper.readValue(result.response.contentAsByteArray, File::class.java)!!
        Assertions.assertNotNull(parsed)
        Assertions.assertNull(parsed.data)
        Assertions.assertEquals("dummy-pdf", parsed.name)
        Assertions.assertEquals("pdf", parsed.type)
        Assertions.assertNotNull(parsed.id)
        val findFile = fileRepository.findByIdWithNull(parsed.id!!)
        Assertions.assertNotNull(findFile)
        Assertions.assertNotNull(findFile!!.data)
    }

    @Test
    fun `create no file`() {
        val defaultRole = dataLoader.createDefaultRole()
        val defaultUser = usersRepository.save(
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
        val articleTypes = dataLoader.createList6ArticleType()
        val tags = dataLoader.createList9Tags()
        val disciplines = dataLoader.createList6Disciplines()
        val article = dataLoader.createArticle(
            "someTitle",
            "somePreview",
            "text",
            articleType = articleTypes[0],
            user = defaultUser,
            tag = tags[0],
            discipline = disciplines[0]
        )
        val token = loginAndGetToken("uservanya", "a12345678")

        mvc.perform(
            MockMvcRequestBuilders.multipart("/files")
                .param("articleId", article.id!!.toString())
                .header("Authorization", "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andReturn()

        val listFile = fileRepository.findAll()
        Assertions.assertTrue(listFile.isEmpty())
    }

    @Test
    fun `read file`() {
        val toCreate = File(
            name = "test",
            username = "user@email.com",
            type = "pdf",
            createdAt = DateTime.now().millis,
            data = byteArrayOf()
        )
        val file: File = fileRepository.save(toCreate)
        val result = mvc.perform(
            MockMvcRequestBuilders.get("/files/{id}", file.id)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        val parsed: File = objectMapper.readValue(result.response.contentAsString, File::class.java)
        Assertions.assertEquals(file.id, parsed.id)
        Assertions.assertEquals(file.name, parsed.name)
        Assertions.assertEquals(file.description, parsed.description)
        Assertions.assertEquals(file.username, parsed.username)
        Assertions.assertEquals(file.type, parsed.type)
        Assertions.assertNotNull(parsed.createdAt)
        Assertions.assertNull(parsed.data)
    }

    @Test
    fun `read notFound`() {
        val id = "NoSuchId"
        mvc.perform(
            MockMvcRequestBuilders.get("/files/{id}", id)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `download file`() {
        val testFile = ResourceUtils.getFile("classpath:dummy-pdf.pdf")
        val toCreate = File(
            name = "test-pdf",
            username = "user@email.com",
            type = "pdf",
            data = Files.readAllBytes(testFile.toPath())
        )
        val file: File = fileRepository.save(toCreate)
        mvc.perform(
            MockMvcRequestBuilders.get("/files/{id}/download", file.id)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
    }

    @Throws(IOException::class)
    private fun createDummyFile(): MockMultipartFile {
        val dummyAttachment = ClassPathResource("dummy-pdf.pdf")
        Assertions.assertTrue(dummyAttachment.isReadable)
        return MockMultipartFile(
            "file",
            "dummy-pdf.pdf",
            MimeTypeUtils.IMAGE_PNG_VALUE,
            FileCopyUtils.copyToByteArray(dummyAttachment.inputStream)
        )
    }
}