package com.polyhabr.poly_back.mapper

import com.polyhabr.poly_back.controller.model.article.request.*
import com.polyhabr.poly_back.controller.model.articleType.request.ArticleTypeRequest
import com.polyhabr.poly_back.controller.model.articleType.request.toDto
import com.polyhabr.poly_back.controller.model.comment.request.CommentRequest
import com.polyhabr.poly_back.controller.model.comment.request.toDtoWithoutUser
import com.polyhabr.poly_back.controller.model.disciplineType.request.DisciplineTypeRequest
import com.polyhabr.poly_back.controller.model.disciplineType.request.toDto
import com.polyhabr.poly_back.controller.model.user.request.UserRequest
import com.polyhabr.poly_back.controller.model.user.request.toDto
import com.polyhabr.poly_back.controller.model.userToLikedArticle.request.UserToLikedArticleRequest
import com.polyhabr.poly_back.controller.model.userToLikedArticle.request.toDto
import com.polyhabr.poly_back.controller.model.userToLikedArticle.response.UserToLikedArticleCreateResponse
import com.polyhabr.poly_back.controller.model.userToLikedArticle.response.UserToLikedArticleListResponse
import com.polyhabr.poly_back.controller.model.userToLikedArticle.response.UserToLikedArticleUpdateResponse
import com.polyhabr.poly_back.controller.model.userToLikedArticle.response.toResponse
import com.polyhabr.poly_back.controller.utils.FieldRequest
import com.polyhabr.poly_back.dto.*
import com.polyhabr.poly_back.entity.*
import com.polyhabr.poly_back.entity.auth.Role
import com.polyhabr.poly_back.entity.auth.User
import com.polyhabr.poly_back.entity.auth.toDto
import com.polyhabr.poly_back.service.UserServiceTest
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.test.context.SpringBootTest
import io.mockk.every
import org.junit.jupiter.api.Assertions.assertEquals
import io.mockk.mockk
import org.joda.time.DateTime

@ExtendWith(MockitoExtension::class)
@SpringBootTest
class MapperTest {
    private companion object {
        const val TEST_ID = 1L
        const val TEST_NAME = "Test name"
        const val TEST_SURNAME = "Test surname"
        const val TEST_LOGIN = "Test login"
        const val TEST_PASSWORD = "Test password"
        const val TEST_EMAIL = "Test email"
        const val TEST_TITLE = "Test title"
        const val TEST_TEXT = "Test text"
        const val TEST_PREVIEW_TEXT = "Test preview text"
        const val TEST_DATE = 123123123L
        const val TEST_LIKES = 1
        const val TEST_IS_FAV = false
        const val TEST_VIEW = 0L
        val TEST_TYPE_ID = null
        val TEST_USER_ID = null

        val defaultRole = Role("ROLE_ADMIN")

        val defaultUser1 = User(
            login = "admin",
            password = "admin",
            name = "dmitry",
            surname = "shabinsky",
            email = "admin@notfake.com",
            enabled = true,
            roles = listOf(defaultRole)
        ).apply {
            id = 1L
        }

        val dArticle = Article(
            title = TEST_TITLE,
            text = TEST_TEXT,
            previewText = TEST_PREVIEW_TEXT,
            date = TEST_DATE,
            likes = TEST_LIKES,
            isFav = TEST_IS_FAV,
            view = TEST_VIEW,
            typeId = TEST_TYPE_ID,
            userId = defaultUser1
        ).apply {
            id = TEST_ID
        }

        val dTagType = TagType(
            name = "Test tag type"
        ).apply {
            id = 1L
        }

        val dDisciplineType = DisciplineType(
            name = "Test discipline type"
        ).apply {
            id = 1L
        }
    }

    @Test
    fun `test UserEntity to UserDto mapping`() {
        val userEntity = defaultUser1

        val expectedDto = UserDto(
            id = userEntity.id,
            name = userEntity.name,
            surname = userEntity.surname,
            login = userEntity.login,
            password = userEntity.password,
            email = userEntity.email
        )
        val actualDto = userEntity.toDto()

        assertEquals(expectedDto, actualDto)
    }

    // write userRequest to UserDto mapping test
    @Test
    fun `test UserRequest to UserDto mapping`() {
        val userRequest = UserRequest(
            name = TEST_NAME,
            surname = TEST_SURNAME,
            login = TEST_LOGIN,
            password = TEST_PASSWORD,
            email = TEST_EMAIL
        )

        val expectedDto = UserDto(
            name = TEST_NAME,
            surname = TEST_SURNAME,
            login = TEST_LOGIN,
            password = TEST_PASSWORD,
            email = TEST_EMAIL
        )

        val actualDto = userRequest.toDto()

        assertEquals(expectedDto, actualDto)
    }

    // write ArticleRequest to ArticleDto mapping test
    @Test
    fun `test ArticleRequest to ArticleDto mapping`() {
        val datedd = DateTime.now()

        val articleRequest = ArticleRequest(
            title = TEST_TITLE,
            text = TEST_TEXT,
            previewText = TEST_PREVIEW_TEXT,
            filePdf = "file.pdf",
            articleType = "article",
            listDisciplineName = listOf(),
            listTag = listOf(),
        )

        val expectedDto = ArticleDto(
            date = datedd,
            likes = 0,
            previewText = articleRequest.previewText!!,
            title = articleRequest.title!!,
            listDisciplineName = articleRequest.listDisciplineName!!,
            listTag = articleRequest.listTag!!,
            typeName = articleRequest.articleType!!,
            text = articleRequest.text!!,
            isSaveToFavourite = false,
            pdfId = null,
            previewImgId = null
        )

        val actualDto = articleRequest.toDtoWithoutType().apply {
            date = datedd
        }

        val someByteArray = ByteArray(2)
        val actualDto2 = articleRequest.toDtoWithoutType(someByteArray,"psgk")

        assertEquals(expectedDto, actualDto)
    }

    // write tests for ArticleEntity to ArticleDto mapping
    @Test
    fun `test ArticleEntity to ArticleDto mapping`() {
        val articleEntity = Article(
            title = "Title",
            text = "Text",
            previewText = "Preview text",
            date = 123123123L,
            likes = 1,
            isFav = false,
            view = 0,
            typeId = null,
            userId = null,
        ).apply {
            id = 1L
        }

        val expectedDto = ArticleDto(
            id = articleEntity.id,
            date = DateTime(articleEntity.date),
            likes = articleEntity.likes,
            previewText = articleEntity.previewText,
            typeId = articleEntity.typeId,
            userId = articleEntity.userId,
            title = articleEntity.title,
            listDisciplineName = listOf(),
            listTag = listOf(),
            text = articleEntity.text,
            fileId = articleEntity.file_id?.id,
            viewCount = articleEntity.view,
            isSaveToFavourite = articleEntity.isFav,
            pdfId = articleEntity.file_id?.id,
            previewImgId = articleEntity.preview_src_id?.id
        )

        val actualDto = articleEntity.toDto(
            listOf(),
            listOf()
        )

        assertEquals(expectedDto, actualDto)
    }

    // write test toDto() for CommentEntity
    @Test
    fun `test CommentEntity to CommentDto mapping`() {
        val commentEntity = Comment(
            text = "Text",
            date = 123123123L,
            articleId = null,
            userId = null,
        ).apply {
            id = 1L
        }

        val expectedDto = CommentDto(
            id = commentEntity.id,
            text = commentEntity.text,
            articleId = commentEntity.articleId,
            userId = commentEntity.userId,
            data = DateTime(commentEntity.date)
        )

        val actualDto = commentEntity.toDto()

        assertEquals(expectedDto, actualDto)
    }

    // write test for CommentRequest to CommentDto mapping
    @Test
    fun `test CommentRequest to CommentDto mapping`() {
        val dateTime = DateTime.now()

        val commentRequest = CommentRequest(
            text = "Text",
            articleId = null,
        )

        val expectedDto = CommentDto(
            text = commentRequest.text,
            data = dateTime,
        )

        val actualDto = commentRequest.toDtoWithoutUser().apply {
            data = dateTime
        }

        assertEquals(expectedDto, actualDto)
    }

    // write test toDto() for ArticleToDisciplineEntity
    @Test
    fun `test ArticleToDisciplineEntity to ArticleToDisciplineDto mapping`() {
        val articleToDisciplineEntity = ArticleToDisciplineType(
            article = dArticle,
            disciplineType = dDisciplineType,
        ).apply {
            id = 1L
        }

        val expectedDto = ArticleToDisciplineTypeDto(
            id = articleToDisciplineEntity.id,
            articleId = articleToDisciplineEntity.article,
            disciplineTypeId = articleToDisciplineEntity.disciplineType,
        )

        val actualDto = articleToDisciplineEntity.toDto()

        assertEquals(expectedDto, actualDto)
    }

    // write test toDto()  for ArticleToTagEntity
    @Test
    fun `test ArticleToTagEntity to ArticleToTagDto mapping`() {
        val articleToTagEntity = ArticleToTagType(
            article = null,
            tagType = null,
        ).apply {
            id = 1L
        }

        val expectedDto = ArticleToTagTypeDto(
            id = articleToTagEntity.id,
            articleId = articleToTagEntity.article,
            tagTypeId = articleToTagEntity.tagType,
        )

        val actualDto = articleToTagEntity.toDto()

        assertEquals(expectedDto, actualDto)
    }

    // write test for ArticleToDisciplineTypeRequest to ArticleToDisciplineTypeDto mapping

    // write test toDto() for ArticleType
    @Test
    fun `test ArticleTypeEntity to ArticleTypeDto mapping`() {
        val articleTypeEntity = ArticleType(
            name = "Name",
        ).apply {
            id = 1L
        }

        val expectedDto = ArticleTypeDto(
            id = articleTypeEntity.id,
            name = articleTypeEntity.name,
        )

        val actualDto = articleTypeEntity.toDto()

        assertEquals(expectedDto, actualDto)
    }

    // write test ArticleTypeRequest to ArticleTypeDto mapping
    @Test
    fun `test ArticleTypeRequest to ArticleTypeDto mapping`() {
        val articleTypeRequest = ArticleTypeRequest(
            name = "Name",
        )

        val expectedDto = ArticleTypeDto(
            name = articleTypeRequest.name,
        )

        val actualDto = articleTypeRequest.toDto()

        assertEquals(expectedDto, actualDto)
    }

    // write test toDto() for DisciplineType
    @Test
    fun `test DisciplineTypeEntity to DisciplineTypeDto mapping`() {
        val disciplineTypeEntity = DisciplineType(
            name = "Name",
        ).apply {
            id = 1L
        }

        val expectedDto = DisciplineTypeDto(
            id = disciplineTypeEntity.id!!,
            name = disciplineTypeEntity.name!!,
        )

        val actualDto = disciplineTypeEntity.toDto()

        assertEquals(expectedDto, actualDto)
    }

    // write test DisciplineTypeRequest to DisciplineTypeDto mapping
    @Test
    fun `test DisciplineTypeRequest to DisciplineTypeDto mapping`() {
        val disciplineTypeRequest = DisciplineTypeRequest(
            name = "Name",
        )

        val expectedDto = DisciplineTypeDto(
            name = disciplineTypeRequest.name!!,
        )

        val actualDto = disciplineTypeRequest.toDto()

        assertEquals(expectedDto, actualDto)
    }

    @Test
    fun `test TagTypeEntity to TagTypeDto mapping`() {
        val tagTypeEntity = TagType(
            name = "Name",
        ).apply {
            id = 1L
        }

        val expectedDto = TagTypeDto(
            id = tagTypeEntity.id!!,
            name = tagTypeEntity.name!!,
        )

        val actualDto = tagTypeEntity.toDto()

        assertEquals(expectedDto, actualDto)
    }

    // write test toDto() for UserToLikeArticleEntity
    @Test
    fun `test UserToLikeArticleEntity to UserToLikeArticleDto mapping`() {
        val userToLikeArticleEntity = UserToLikedArticle(
            article = null,
            user = null,
        ).apply {
            id = 1L
        }

        val expectedDto = UserToLikedArticleDto(
            id = userToLikeArticleEntity.id,
            articleId = userToLikeArticleEntity.article,
            userId = userToLikeArticleEntity.user,
        )

        val actualDto = userToLikeArticleEntity.toDto()

        assertEquals(expectedDto, actualDto)
    }

    // write test UserToLikedArticleRequest to UserToLikedArticleDto mapping
    @Test
    fun `test UserToLikedArticleRequest to UserToLikedArticleDto mapping`() {
        val userToLikedArticleRequest = UserToLikedArticleRequest(
            articleId = dArticle,
            userId = defaultUser1,
        )

        val userToLikedArticleCreateResponse = UserToLikedArticleCreateResponse(
            isSuccess = true,
            id = 1L
        )

        val expectedDto = UserToLikedArticleDto(
            articleId = userToLikedArticleRequest.articleId,
            userId = userToLikedArticleRequest.userId,
        )

        val userToLikedArticleListResponse = UserToLikedArticleListResponse(listOf(expectedDto.toResponse()), 1)
        val userToLikedArticleListResponse2 = listOf(expectedDto.toResponse())
        val userToLikedArticleUpdateResponse = UserToLikedArticleUpdateResponse(true)

        val fieldRequest = FieldRequest("nameField", true, "forDataRange")
        val byteArray = ByteArray(5)
        val fileCreatingDto = FileCreatingDto("username", "name", "originalName", "description", byteArray)
        val actualDto = userToLikedArticleRequest.toDto()

        assertEquals(expectedDto, actualDto)
    }

    // write test ArticleUpdateRequest to ArticleUpdateDto mapping
    @Test
    fun `test ArticleUpdateRequest to ArticleUpdateDto mapping`() {
        val articleUpdateRequest = ArticleUpdateRequest(
            title = "Title",
            text = "Text",
            previewText = "PreviewText",
            filePdf = "FilePdf",
            likes = 1,
            typeName = "TypeName",
        )

        val expectedDto = ArticleUpdateDto(
            title = articleUpdateRequest.title,
            text = articleUpdateRequest.text,
            previewText = articleUpdateRequest.previewText,
            filePdf = articleUpdateRequest.filePdf,
            likes = articleUpdateRequest.likes,
            typeName = articleUpdateRequest.typeName,
        )

        val actualDto = articleUpdateRequest.toDto()

        val actualDto2 = articleUpdateRequest.toDtoOnlyLike()

        assertEquals(expectedDto, actualDto)
    }
}