package com.polyhabr.poly_back.integration.scenarios

import com.polyhabr.poly_back.controller.auth.LoginUser
import com.polyhabr.poly_back.controller.model.article.response.ArticleListResponse
import com.polyhabr.poly_back.controller.model.article.response.ArticleResponse
import com.polyhabr.poly_back.repository.ArticleRepository
import com.polyhabr.poly_back.repository.TagTypeRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper
import java.util.*


@Testcontainers
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
class DefaultIT {
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

    private val objectMapper = ObjectMapper()

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @Autowired
    private lateinit var tagTypeRepository: TagTypeRepository

    @Autowired
    private lateinit var articleRepository: ArticleRepository

    @Test
    fun accessApplication() {
    }

    private fun loginAndGetToken(): String {
        val loginUser = LoginUser("admin", "admincool")
        val loginResponseRaw =
            testRestTemplate.postForObject("/api/auth/signin", HttpEntity(loginUser, null), String::class.java)
        val loginResponse = objectMapper.readTree(loginResponseRaw)
        val token = loginResponse.get("accessToken")
        return token.textValue()
    }

    private fun createDefaultHeaders(token: String? = null) =
        HttpHeaders().apply {
            accept = listOf(MediaType.APPLICATION_JSON)
            add("Content-Type", "application/json")
            token?.let {
                add("X-Authorization", "Bearer $it")
            }
        }

    @Test
    fun `Find an article in the search and go to it`() {
        val offsetForShow = 0
        val sizeForShow = 100
        val showedListArticleResponse = testRestTemplate.postForEntity(
            "/articles?offset=${offsetForShow}&size=${sizeForShow}",
            HttpEntity(null, createDefaultHeaders()),
            ArticleListResponse::class.java
        )
        val bodyShowedListArticleResponse = showedListArticleResponse.body

        Assertions.assertNotNull(bodyShowedListArticleResponse)
        Assertions.assertTrue(bodyShowedListArticleResponse!!.contents.isNotEmpty())

        val showedArticleList = bodyShowedListArticleResponse.contents
        val randomIdArticle = showedArticleList.indices.random()
        val randomArticle = showedArticleList[randomIdArticle]
        val randomTitle = randomArticle.title

        val offsetForSearch = 0
        val sizeForSearch = Int.MAX_VALUE
        val articleSearchResponse = testRestTemplate.getForEntity(
            "/articles/searchByTittle?prefix=${randomTitle}&offset=${offsetForSearch}&size=${sizeForSearch}",
            ArticleListResponse::class.java
        )

        Assertions.assertNotNull(articleSearchResponse.body)
        Assertions.assertTrue(articleSearchResponse.body!!.contents.isNotEmpty())

        val searchListArticle = articleSearchResponse.body!!.contents
        val foundArticle = searchListArticle.find { it.id == randomArticle.id }

        Assertions.assertNotNull(foundArticle)

        val articleDetailResponse = testRestTemplate.getForEntity(
            "/articles/byId?id=${foundArticle!!.id}",
            ArticleResponse::class.java
        )
        val detailArticle = articleDetailResponse.body

        Assertions.assertNotNull(detailArticle)
        Assertions.assertEquals(foundArticle.id, detailArticle!!.id)
        Assertions.assertEquals(randomTitle, detailArticle.title)
    }
}