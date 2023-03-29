package com.polyhabr.poly_back.unit.service

import com.polyhabr.poly_back.controller.model.articleType.request.ArticleTypeRequest
import com.polyhabr.poly_back.entity.ArticleType
import com.polyhabr.poly_back.entity.auth.Role
import com.polyhabr.poly_back.entity.auth.User
import com.polyhabr.poly_back.entity.toDto
import com.polyhabr.poly_back.repository.ArticleTypeRepository
import com.polyhabr.poly_back.service.impl.ArticleTypeServiceImpl
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
class ArticleTypeServiceTest {
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

        val pageRequest = PageRequest.of(0, Int.MAX_VALUE)

        val defaultArticleType1 = ArticleType(
            name = "ArticleType1",
        ).apply {
            id = 1L
        }

        val defaultArticleType2 = ArticleType(
            name = "ArticleType2",
        ).apply {
            id = 2L
        }
    }

    @Mock
    private lateinit var articleTypeRepository: ArticleTypeRepository

    @InjectMocks
    private lateinit var articleTypeService: ArticleTypeServiceImpl

    @Test
    fun `get all article types`() {
        val articleTypes = listOf(defaultArticleType1, defaultArticleType2)
        val expectedArticle = articleTypes.map { it.toDto() }
        val page = PageImpl(articleTypes)

        given(articleTypeRepository.findAll(Mockito.any(PageRequest::class.java))).willReturn(page)

        val result = articleTypeService.getAll(1, 1)
            .content

        Assertions.assertEquals(expectedArticle, result)
    }

    @Test
    fun `get article type by id`() {
        val articleType = defaultArticleType1
        val expectedArticle = articleType.toDto()

        given(articleTypeRepository.findById(any())).willReturn(Optional.of(articleType))

        val result = articleTypeService.getById(1L)

        Assertions.assertEquals(expectedArticle, result)
    }

    @Test
    fun `search by name article type`() {
        val articleTypes = listOf(defaultArticleType1, defaultArticleType2)
        val expectedArticles = articleTypes.map { it.toDto() }
        val page = PageImpl(articleTypes)
        val prefix = "articleType"

        given(articleTypeRepository.findArticleTypesByName(any(), eq(prefix))).willReturn(page)

        val result = articleTypeService.searchByName(prefix, 1, 1)
            .content

        Assertions.assertEquals(expectedArticles, result)
    }

    @Test
    fun `create article type`() {
        val articleType = defaultArticleType1
        val articleTypeRequest = ArticleTypeRequest(
            name = "articleType1",
        )

        given(articleTypeRepository.save(Mockito.any(ArticleType::class.java))).willReturn(articleType)

        val result = articleTypeService.create(articleTypeRequest)

        Assertions.assertNotNull(result)
    }

    @Test
    fun `update article type`() {
        val articleType = defaultArticleType1
        val articleTypeRequest = ArticleTypeRequest(
            name = "articleType1",
        )

        given(articleTypeRepository.findById(any())).willReturn(Optional.of(articleType))
        given(articleTypeRepository.save(Mockito.any(ArticleType::class.java))).willReturn(articleType)

        val result = articleTypeService.update(1L, articleTypeRequest)

        Assertions.assertNotNull(result)
    }

    @Test
    fun `delete articleType`() {
        val result = articleTypeService.delete(1L)

        Assertions.assertNotNull(result)
    }
}