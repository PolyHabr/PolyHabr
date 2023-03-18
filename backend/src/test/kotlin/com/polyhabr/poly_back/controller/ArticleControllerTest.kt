package com.polyhabr.poly_back.controller

import com.polyhabr.poly_back.controller.model.article.request.ArticleRequest
import com.polyhabr.poly_back.controller.model.article.request.ArticleUpdateRequest
import com.polyhabr.poly_back.controller.model.article.request.SortArticleRequest
import com.polyhabr.poly_back.controller.model.article.response.ArticleCreateResponse
import com.polyhabr.poly_back.controller.model.article.response.ArticleUpdateResponse
import com.polyhabr.poly_back.controller.model.article.response.toListResponse
import com.polyhabr.poly_back.controller.model.article.response.toResponse
import com.polyhabr.poly_back.entity.*
import com.polyhabr.poly_back.entity.auth.Role
import com.polyhabr.poly_back.entity.auth.User
import com.polyhabr.poly_back.service.ArticleService
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.given
import org.springframework.data.domain.PageImpl
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit4.SpringRunner

@ExtendWith(MockitoExtension::class)
@RunWith(SpringRunner::class)
class ArticleControllerTest {
    private companion object {
        val defaultRole = Role("ROLE_ADMIN")

        val defaultUser1 = User(
            login = "admin",
            password = "admin",
            name = "dmitry",
            surname = "shabinsky",
            email = "admin@notfake.com",
            enabled = true,
            roles = listOf(defaultRole),
        ).apply {
            id = 1L
        }

        val defaultTagType = TagType(
            name = "tag",
        ).apply {
            id = 1L
        }

        val defaultDisciplineType = DisciplineType(
            name = "discipline",
        ).apply {
            id = 1L
        }

        val defaultArticleType = ArticleType(
            name = "type",
        ).apply {
            id = 1L
        }

        val myDate = 123123123123L

        val defaultArticle = Article(
            title = "title",
            text = "content",
            previewText = "preview",
            date = myDate,
            likes = 0,
            isFav = false,
            view = 0,
            typeId = defaultArticleType,
            userId = defaultUser1,
        ).apply {
            id = 1L
        }

        val articles = listOf(
            defaultArticle,
        )
        val disciplineTypes = listOf(
            defaultDisciplineType,
        ).map { it.name.toString() }

        val tagTypes = listOf(
            defaultTagType,
        ).map { it.name.toString() }

        val articleToDisciplineTypes = listOf(
            ArticleToDisciplineType(
                article = defaultArticle,
                disciplineType = defaultDisciplineType
            ),
        )

        val articleToTagTypes = listOf(
            ArticleToTagType(
                article = defaultArticle,
                tagType = defaultTagType
            ),
        )
    }

    @Mock
    private lateinit var articleService: ArticleService

    @InjectMocks
    private lateinit var articleController: ArticleController

    // write test get all articles
    @Test
    fun `test get all articles`() {
        val article = defaultArticle
        val dtoArticle = article.toDto(disciplineTypes, tagTypes)
        val page = PageImpl(listOf(dtoArticle))
        val sort = SortArticleRequest()

        given(articleService.getAll(1, 1,sort)).willReturn(page)

        val expectedResponse = ResponseEntity(page.toListResponse(), HttpStatus.OK)
        val actualResponse = articleController.getAll(1, 1, sort)

        assertEquals(expectedResponse, actualResponse)
    }

    // write test get article by id
    @Test
    fun `test get article by id`() {
        val article = defaultArticle
        val dtoArticle = article.toDto(disciplineTypes, tagTypes)

        given(articleService.getById(1)).willReturn(dtoArticle)

        val expectedResponse = ResponseEntity(dtoArticle.toResponse(), HttpStatus.OK)
        val actualResponse = articleController.getById(1)

        assertEquals(expectedResponse, actualResponse)
    }

    // write test search articles by title
    @Test
    fun `test search articles by title`() {
        val article = defaultArticle
        val dtoArticle = article.toDto(disciplineTypes, tagTypes)
        val page = PageImpl(listOf(dtoArticle))

        given(articleService.searchByName("title", 1, 1, null)).willReturn(page)

        val expectedResponse = ResponseEntity(page.toListResponse(), HttpStatus.OK)
        val actualResponse = articleController.searchArticlesByTittle("title", 1, 1)

        assertEquals(expectedResponse, actualResponse)
    }

    // write test article create
    @Test
    fun `test article create`() {
        val article = defaultArticle
        val articleDto = article.toDto(disciplineTypes, tagTypes).apply {
            typeName = defaultArticleType.name.toString()
        }
        val expected = true to 1L

        val articleRequest = ArticleRequest(
            title = articleDto.title,
            text = articleDto.text,
            previewText = articleDto.previewText,
            listDisciplineName = articleDto.listDisciplineName,
            listTag = articleDto.listTag,
            articleType = articleDto.typeName,
        )

        given(articleService.create(any())).willReturn(expected)

        val expectedResponse = ResponseEntity(ArticleCreateResponse(true, 1L), HttpStatus.OK)
        val actualResponse = articleController.create(articleRequest)

        assertEquals(expectedResponse, actualResponse)
    }

    // write test article update
    @Test
    fun `test article update`() {
        val article = defaultArticle
        val articleDto = article.toDto(disciplineTypes, tagTypes).apply {
            typeName = defaultArticleType.name.toString()
        }
        val expected = true to "success"

        val articleRequest = ArticleUpdateRequest(
            title = "12312312312312"
        )

        given(articleService.update(any(), any())).willReturn(expected)

        val expectedResponse = ResponseEntity(
            ArticleUpdateResponse(
                true,
                "success"
            ), HttpStatus.OK
        )
        val actualResponse = articleController.update(1, articleRequest)

        assertEquals(expectedResponse, actualResponse)
    }

}