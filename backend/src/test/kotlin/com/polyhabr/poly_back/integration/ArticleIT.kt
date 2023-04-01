package com.polyhabr.poly_back.integration

import com.polyhabr.poly_back.config.DataLoader
import com.polyhabr.poly_back.controller.auth.LoginUser
import com.polyhabr.poly_back.controller.model.article.request.ArticleRequest
import com.polyhabr.poly_back.controller.model.article.response.ArticleCreateResponse
import com.polyhabr.poly_back.controller.model.article.response.ArticleListResponse
import com.polyhabr.poly_back.controller.model.article.response.ArticleResponse
import com.polyhabr.poly_back.entity.Article
import com.polyhabr.poly_back.entity.auth.User
import com.polyhabr.poly_back.repository.ArticleRepository
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
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
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
class ArticleIT @Autowired constructor(
    private final val testRestTemplate: TestRestTemplate,
    private final val dataLoader: DataLoader,
    val articleRepository: ArticleRepository,
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
    fun `get articles`() {
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
        val articleList = mutableListOf<Article>()
        val articleTypes = dataLoader.createList6ArticleType()
        val tags = dataLoader.createList9Tags()
        val disciplines = dataLoader.createList6Disciplines()

        for (i in 0 until 5) {
            articleList.add(
                dataLoader.createArticle(
                    "someTitle$i",
                    "somePreview$i",
                    "text$i",
                    articleType = articleTypes[0],
                    user = defaultUser,
                    tag = tags[0],
                    discipline = disciplines[0]
                )
            )
        }

        val offset = 0
        val size = Int.MAX_VALUE
        val response = testRestTemplate.postForEntity(
            "/articles?offset=${offset}&size=${size}",
            HttpEntity(null, createDefaultHeaders()),
            ArticleListResponse::class.java
        )

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        Assertions.assertNotNull(response.body)

        val responseArticles = response.body!!.contents

        Assertions.assertEquals(articleList.size, responseArticles.size)
        articleList.forEachIndexed { index, article ->
            Assertions.assertEquals(articleList[index].title, article.title)
            Assertions.assertEquals(articleList[index].previewText, article.previewText)
            Assertions.assertEquals(articleList[index].text, article.text)
            Assertions.assertEquals(articleList[index].date, article.date)
        }
    }

    @Test
    fun `open detail article success`() {
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

        val response = testRestTemplate.getForEntity(
            "/articles/byId?id=${article.id}",
            ArticleResponse::class.java
        )

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        Assertions.assertNotNull(response.body)

        val detailArticle = response.body!!

        Assertions.assertEquals(article.id, detailArticle.id)
        Assertions.assertEquals(article.text, detailArticle.text)
        Assertions.assertEquals(article.userId?.id, detailArticle.user?.id)
    }

    @Test
    fun `open non - existent detail article`() {
        val response = testRestTemplate.getForEntity(
            "/articles/byId?id=${(1..Int.MAX_VALUE).random()}",
            ArticleResponse::class.java
        )

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun `search articles by title`() {
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
        val articleList = mutableListOf<Article>()
        val articleTypes = dataLoader.createList6ArticleType()
        val tags = dataLoader.createList9Tags()
        val disciplines = dataLoader.createList6Disciplines()

        for (i in 0 until 40) {
            articleList.add(
                dataLoader.createArticle(
                    "someTitle$i",
                    "somePreview$i",
                    "text$i",
                    articleType = articleTypes[0],
                    user = defaultUser,
                    tag = tags[0],
                    discipline = disciplines[0]
                )
            )
        }

        val randomPick = articleList.indices.random()

        val offset = 0
        val size = Int.MAX_VALUE
        val response = testRestTemplate.getForEntity(
            "/articles/searchByTittle?offset=${offset}&size=${size}&prefix=${articleList[randomPick].title}",
            ArticleListResponse::class.java
        )

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        Assertions.assertNotNull(response.body)

        val responseArticles = response.body!!.contents
        Assertions.assertEquals(1, responseArticles.size)
        val responseArticle = responseArticles[0]

        Assertions.assertEquals(articleList[randomPick].title, responseArticle.title)
        Assertions.assertEquals(articleList[randomPick].previewText, responseArticle.previewText)
        Assertions.assertEquals(articleList[randomPick].text, responseArticle.text)
        Assertions.assertEquals(articleList[randomPick].userId?.id, responseArticle.user?.id)
    }

    @Test
    fun `create article is success`() {
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
        val token = loginAndGetToken("uservanya", "a12345678")

        val articleRequest = ArticleRequest(
            title = "title",
            text = "text",
            previewText = "previewText",
            listDisciplineName = disciplines.map { it.name!! }.subList(0, 2),
            listTag = tags.map { it.name!! }.subList(0, 2),
            articleType = articleTypes[0].name!!,
        )

        val response = testRestTemplate.postForEntity(
            "/articles/create",
            HttpEntity(articleRequest, createDefaultHeaders(token)),
            ArticleCreateResponse::class.java
        )

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        Assertions.assertNotNull(response.body)

        val createdArticle = response.body!!
        Assertions.assertTrue(createdArticle.isSuccess)
        Assertions.assertNotNull(createdArticle.id)

        val articleFromDB = articleRepository.findByIdOrNull(createdArticle.id)
        Assertions.assertNotNull(articleFromDB)
    }

    @Test
    fun `create article is failure 401`() {
        val defaultRole = dataLoader.createDefaultRole()
        usersRepository.save(
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

        val articleRequest = ArticleRequest(
            title = "title",
            text = "text",
            previewText = "previewText",
            listDisciplineName = disciplines.map { it.name!! }.subList(0, 2),
            listTag = tags.map { it.name!! }.subList(0, 2),
            articleType = articleTypes[0].name!!,
        )

        val response = testRestTemplate.postForEntity(
            "/articles/create",
            HttpEntity(articleRequest, createDefaultHeaders()),
            ArticleCreateResponse::class.java
        )

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
    }
}