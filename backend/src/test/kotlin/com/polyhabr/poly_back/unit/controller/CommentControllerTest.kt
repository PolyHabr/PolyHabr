package com.polyhabr.poly_back.unit.controller

import com.polyhabr.poly_back.controller.CommentController
import com.polyhabr.poly_back.controller.model.comment.request.CommentRequest
import com.polyhabr.poly_back.controller.model.comment.response.CommentCreateResponse
import com.polyhabr.poly_back.controller.model.comment.response.CommentUpdateResponse
import com.polyhabr.poly_back.controller.model.comment.response.toListResponse
import com.polyhabr.poly_back.controller.model.comment.response.toResponse
import com.polyhabr.poly_back.entity.*
import com.polyhabr.poly_back.entity.auth.Role
import com.polyhabr.poly_back.entity.auth.User
import com.polyhabr.poly_back.unit.service.ArticleServiceTest
import com.polyhabr.poly_back.service.CommentService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.given
import org.springframework.data.domain.PageImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import javax.validation.ConstraintViolationException

@ExtendWith(MockitoExtension::class)
class CommentControllerTest {
    @Mock
    private lateinit var commentService: CommentService

    @InjectMocks
    private lateinit var commentController: CommentController

    companion object {
        const val DEFAULT_PAGE = 1
        const val DEFAULT_SIZE = 1

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
            userId = ArticleServiceTest.defaultUser1,
        ).apply {
            id = 1L
        }

        val defaultComment = Comment(
            text = "text",
            date = myDate,
            userId = defaultUser1,
            articleId = defaultArticle,
        ).apply {
            id = 1L
        }
    }

    // write test get all comments
    @Test
    fun `test get all comments`() {
        val comment = defaultComment
        val commentDto = comment.toDto()
        val list = listOf(commentDto)
        val page = PageImpl(list)

        given(commentService.getAll(DEFAULT_PAGE, DEFAULT_SIZE)).willReturn(page)

        val excepted = ResponseEntity.ok(page.toListResponse())
        val actual = commentController.getAll(DEFAULT_PAGE, DEFAULT_SIZE)

        assertEquals(excepted, actual)
    }

    // write test get comments by id
    @Test
    fun `test get comments by id`() {

        val comment = defaultComment
        val commentDto = comment.toDto()

        given(commentService.getById(defaultComment.id!!)).willReturn(defaultComment.toDto())

        val excepted = ResponseEntity.ok(commentDto.toResponse())
        val actual = commentController.getById(defaultComment.id!!)

        assertEquals(excepted, actual)
    }

    // write test create comment
    @Test
    fun `test create comment`() {
        val comment = defaultComment
        val commentRequest = CommentRequest(
            text = comment.text,
            articleId = comment.articleId!!.id!!,
        )

        given(commentService.create(commentRequest)).willReturn(comment.id)

        val excepted = ResponseEntity.ok(CommentCreateResponse(id = comment.id!!, isSuccess = true))
        val actual = commentController.create(commentRequest)

        assertEquals(excepted, actual)
    }

    // write test update comment
    @Test
    fun `test update comment`() {
        val updateCommentRequest = CommentRequest(
            text = "new text",
            articleId = defaultArticle.id!!,
        )
        val comment = defaultComment

        given(commentService.update(comment.id!!, updateCommentRequest)).willReturn(true to "success")

        val excepted = ResponseEntity.ok(CommentUpdateResponse(message = "success", isSuccess = true))
        val actual = commentController.update(comment.id!!, updateCommentRequest)

        assertEquals(excepted, actual)
    }

    @Test
    fun `test get comments by article id`() {

        val comment = defaultComment
        val commentDto = comment.toDto()
        val page = PageImpl(listOf(commentDto))

        given(commentService.getByArticleIdAll(1, 1, 1)).willReturn(page)

        val excepted = ResponseEntity.ok(page.toListResponse())
        val actual = commentController.getByArticleId(1,1,1)

        assertEquals(excepted, actual)
    }

    @Test
    fun `test search comments by name`() {

        val comment = defaultComment
        val commentDto = comment.toDto()
        val page = PageImpl(listOf(commentDto))

        given(commentService.searchByName("text", 1, 1)).willReturn(page)

        val excepted = ResponseEntity.ok(page.toListResponse())
        val actual = commentController.searchCommentsByName("text",1,1)

        assertEquals(excepted, actual)
    }

    @Test
    fun `test delete comments`() {
        val expected = true to "success"

        given(commentService.delete(1)).willReturn(expected)

        val excepted = ResponseEntity.ok("success")
        val actual = commentController.delete(1)

        assertEquals(excepted, actual)
    }

    @Test
    fun `test delete non-existing comments`() {
        val expected = false to "INTERNAL_SERVER_ERROR Internal Server Error"

        given(commentService.delete(1)).willReturn(expected)
        val expectedResponse = ResponseEntity("INTERNAL_SERVER_ERROR Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR)

        val actualResponse = commentController.delete(1)
        assertEquals(expectedResponse, actualResponse)

    }

    @Test
    fun `test handleConstraintViolationException`() {
        val exString = "exString"
        val ex = ConstraintViolationException(exString, null)

        val actual = commentController.handleConstraintViolationException(ex)!!
        val expectedBody = "not valid due to validation error: " + ex.message

        assertEquals(actual.statusCode, HttpStatus.BAD_REQUEST)
        assertEquals(actual.body, expectedBody)
    }
}