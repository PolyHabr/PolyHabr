package com.polyhabr.poly_back.unit.service

import com.polyhabr.poly_back.entity.*
import com.polyhabr.poly_back.entity.auth.Role
import com.polyhabr.poly_back.entity.auth.User
import com.polyhabr.poly_back.repository.ArticleRepository
import com.polyhabr.poly_back.repository.CommentRepository
import com.polyhabr.poly_back.repository.auth.UsersRepository
import com.polyhabr.poly_back.service.impl.CommentServiceImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.given
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*

@ExtendWith(MockitoExtension::class)
class CommentServiceTest {
    private companion object {
        val defaultRole = Role("ROLE_ADMIN")

        val defaultWithoutId = User(
            login = "admin123",
            password = "admin123",
            name = "dmitry",
            surname = "shabinsky",
            email = "admin123@notfake.com",
            enabled = true,
            roles = listOf(defaultRole)
        )

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

        val defaultUser2 = User(
            login = "admin1",
            password = "admin1",
            name = "dmitry",
            surname = "shabinsky",
            email = "admin1@notfake.com",
            enabled = false,
            roles = listOf(defaultRole)
        ).apply {
            id = 2L
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

        val pageRequest = PageRequest.of(0, Int.MAX_VALUE)

        val defaultComment1 = Comment(
            date = 1949,
            articleId = defaultArticle,
            userId = defaultUser1
        ).apply {
            id = 1L
        }

        val defaultComment2 = Comment(
            date = 19496,
            articleId = defaultArticle,
            userId = defaultUser2
        ).apply {
            id = 2L
        }
    }

    @Mock
    private lateinit var commentRepository: CommentRepository

    @Mock
    private lateinit var usersRepository: UsersRepository

    @Mock
    private lateinit var articleRepository: ArticleRepository

    @InjectMocks
    private lateinit var commentService: CommentServiceImpl

    @Test
    fun `get all comment`() {
        val comment = listOf(defaultComment1, defaultComment2)
        val expectedComment = comment.map { it.toDto() }
        val page = PageImpl(comment)

        given(commentRepository.findAll(Mockito.any(PageRequest::class.java))).willReturn(page)

        val result = commentService.getAll(1, 1)
            .content

        Assertions.assertEquals(expectedComment, result)
    }

    @Test
    fun `get comment by id`() {
        val comment = defaultComment1
        val expectedComment = comment.toDto()

        given(commentRepository.findById(any())).willReturn(Optional.of(comment))

        val result = commentService.getById(1L)

        Assertions.assertEquals(expectedComment, result)
    }

    @Test
    fun `get comment by article id all`() {
        val comment = listOf(defaultComment1, defaultComment2)
        val expectedComment = comment.map { it.toDto() }
        val page = PageImpl(comment)

        given(commentRepository.findAllByArticleId(any(), Mockito.anyLong())).willReturn(page)

        val result = commentService.getByArticleIdAll(1, 1, 1L).content

        Assertions.assertEquals(expectedComment, result)
    }

    @Test
    fun `search by name comment type`() {
        val comment = listOf(defaultComment1, defaultComment2)
        val expectedComment = comment.map { it.toDto() }
        val page = PageImpl(comment)
        val prefix = "comment"

        given(commentRepository.findCommentsByName(any(), eq(prefix))).willReturn(page)

        val result = commentService.searchByName(prefix, 1, 1)
            .content

        Assertions.assertEquals(expectedComment, result)
    }

//    @Test
//    fun `create comment type`() {
//        val comment = defaultComment1
//        val commentRequest = CommentRequest(
//            text = "comment1",
//            articleId = 1
//        )
//
//        given(commentRepository.save(Mockito.any(Comment::class.java))).willReturn(comment)
//
//        val result = commentService.create(commentRequest)
//
//        Assertions.assertNotNull(result)
//    }

//    @Test
//    fun `update comment`() {
//        val comment = CommentServiceTest.defaultComment1
//        val commentRequest = CommentRequest(
//            text = "comment2",
//            articleId = 1
//        )
//
//        given(commentRepository.findById(any())).willReturn(Optional.of(comment))
//        given(commentRepository.save(Mockito.any(Comment::class.java))).willReturn(comment)
//
//        val result = commentService.update(1L, commentRequest)
//
//        Assertions.assertNotNull(result)
//    }

    @Test
    fun `delete comment`() {
        val result = commentService.delete(1L)

        Assertions.assertNotNull(result)
    }
}