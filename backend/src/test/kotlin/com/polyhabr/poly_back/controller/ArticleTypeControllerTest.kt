package com.polyhabr.poly_back.controller

import com.polyhabr.poly_back.controller.model.articleType.request.ArticleTypeRequest
import com.polyhabr.poly_back.controller.model.articleType.response.*
import com.polyhabr.poly_back.entity.ArticleType
import com.polyhabr.poly_back.entity.toDto
import com.polyhabr.poly_back.service.ArticleTypeService
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.given
import org.springframework.data.domain.PageImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit4.SpringRunner
import javax.validation.ConstraintViolationException

@ExtendWith(MockitoExtension::class)
@RunWith(SpringRunner::class)
class ArticleTypeControllerTest {
    @Mock
    private lateinit var articleTypeService: ArticleTypeService

    @InjectMocks
    private lateinit var articleTypeController: ArticleTypeController

    companion object {
        const val DEFAULT_PAGE = 1
        const val DEFAULT_SIZE = 1

        val defaultArticleType = ArticleType(
            name = "article",
        ).apply {
            id = 1L
        }
    }

    // write test get all article types
    @Test
    fun `test get all article types`() {
        val articleType = defaultArticleType
        val dtoArticleType = articleType.toDto()
        val listOf = listOf(dtoArticleType)
        val page = PageImpl(listOf)

        given(articleTypeService.getAll(DEFAULT_PAGE, DEFAULT_SIZE)).willReturn(page)

        val expectedResponse = ResponseEntity.ok(page.toListResponse())
        val actualResponse = articleTypeController.getAll(DEFAULT_PAGE, DEFAULT_SIZE)

        assertEquals(expectedResponse, actualResponse)
    }

    // write test get article types by id
    @Test
    fun `test get article types by id`() {
        val articleType = defaultArticleType
        val dtoArticleType = articleType.toDto()

        given(articleTypeService.getById(1L)).willReturn(dtoArticleType)

        val expectedResponse = ResponseEntity.ok(
            dtoArticleType.toResponse()
        )
        val actualResponse = articleTypeController.getById(1L)

        assertEquals(expectedResponse, actualResponse)
    }

    // write test create article type
    @Test
    fun `test create article type`() {
        val articleType = defaultArticleType
        val articleTypeRequest = ArticleTypeRequest(
            name = articleType.name,
        )

        given(articleTypeService.create(articleTypeRequest)).willReturn(1L)

        val expectedResponse = ResponseEntity.ok(
            ArticleTypeCreateResponse(id = 1L, isSuccess = true)
        )
        val actualResponse = articleTypeController.create(articleTypeRequest)

        assertEquals(expectedResponse, actualResponse)
    }

    // write test update article type
    @Test
    fun `test update article type`() {
        val articleType = defaultArticleType
        val articleTypeRequest = ArticleTypeRequest(
            name = articleType.name,
        )

        given(articleTypeService.update(1L, articleTypeRequest)).willReturn(true to "success")

        val expectedResponse = ResponseEntity.ok(
            ArticleTypeUpdateResponse(message = "success", isSuccess = true)
        )
        val actualResponse = articleTypeController.update(1L, articleTypeRequest)

        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `test search articles by name`() {
        val articleType = defaultArticleType
        val dtoArticleType = articleType.toDto()

        val page = PageImpl(listOf(dtoArticleType))

        given(articleTypeService.searchByName("article", 1, 1)).willReturn(page)

        val expectedResponse = ResponseEntity(page.toListResponse(), HttpStatus.OK)
        val actualResponse = articleTypeController.searchArticleTypesByName("article", 1, 1)

        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `test delete articleType`() {
        val expected = true to "success"

        given(articleTypeService.delete(1)).willReturn(expected)

        val expectedResponse = ResponseEntity("success", HttpStatus.OK)

        val actualResponse = articleTypeController.delete(1)

        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `test delete non-existing articleType`() {
        val expected = false to "INTERNAL_SERVER_ERROR Internal Server Error"

        given(articleTypeService.delete(1)).willReturn(expected)
        val expectedResponse = ResponseEntity("INTERNAL_SERVER_ERROR Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR)

        val actualResponse = articleTypeController.delete(1)
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `test handleConstraintViolationException`() {
        val exString = "exString"
        val ex = ConstraintViolationException(exString, null)

        val actual = articleTypeController.handleConstraintViolationException(ex)!!
        val expectedBody = "not valid due to validation error: " + ex.message

        assertEquals(actual.statusCode, HttpStatus.BAD_REQUEST)
        assertEquals(actual.body, expectedBody)
    }
}