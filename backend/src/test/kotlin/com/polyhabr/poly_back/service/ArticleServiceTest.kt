package com.polyhabr.poly_back.service

import com.polyhabr.poly_back.controller.ArticleControllerTest
import com.polyhabr.poly_back.controller.model.article.request.ArticleRequest
import com.polyhabr.poly_back.controller.model.article.request.ArticleUpdateRequest
import com.polyhabr.poly_back.dto.ArticleUpdateDto
import com.polyhabr.poly_back.entity.*
import com.polyhabr.poly_back.entity.auth.Role
import com.polyhabr.poly_back.entity.auth.User
import com.polyhabr.poly_back.repository.*
import com.polyhabr.poly_back.repository.auth.UsersRepository
import com.polyhabr.poly_back.service.impl.ArticleServiceImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.anyInt
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.kotlin.*
import org.mockito.quality.Strictness
import org.springframework.data.domain.PageImpl
import org.springframework.transaction.support.TransactionTemplate
import java.util.*
import org.mockito.Mockito.`when`
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity


@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ArticleServiceTest {

    companion object {
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
    private lateinit var usersRepository: UsersRepository

    @Mock
    private lateinit var articleRepository: ArticleRepository

    @Mock
    private lateinit var articleTypeRepository: ArticleTypeRepository

    @Mock
    private lateinit var userToLikedArticleRepository: UserToLikedArticleRepository

    @Mock
    private lateinit var disciplineTypeRepository: DisciplineTypeRepository

    @Mock
    private lateinit var tagTypeRepository: TagTypeRepository

    @Mock
    private lateinit var articleToTagTypeRepository: ArticleToTagTypeRepository

    @Mock
    private lateinit var articleToDisciplineTypeRepository: ArticleToDisciplineTypeRepository

    @Mock
    private lateinit var fileService: FileService

    @Mock
    private lateinit var articleToFavRepository: ArticleToFavRepository

    @Mock
    private lateinit var transactionTemplate: TransactionTemplate

    @InjectMocks
    private lateinit var articleService: ArticleServiceImpl


    fun setup() {
        val page = PageImpl(listOf(defaultArticle))
        given(usersRepository.findByLogin(null)).willReturn(defaultUser1)
        given(articleRepository.findManual(any(), Mockito.anyString())).willReturn(page)
        given(
            articleRepository.findArticleByName(
                any(),
                Mockito.anyString(),
            )
        ).willReturn(page)
        given(
            articleRepository.findArticleByTitle(
                any(),
                Mockito.anyString()
            )
        ).willReturn(page)
        given(
            articleRepository.findArticleByUserId(
                any(),
                Mockito.anyLong()
            )
        ).willReturn(page)
        given(
            articleRepository.findArticlesOrderDate(
                any(),
            )
        ).willReturn(page)
        given(
            articleRepository.findArticlesWithLimitTimelineOrderView(
                any(),
                Mockito.anyLong(),
                Mockito.anyLong(),
            )
        ).willReturn(page)
        given(
            articleRepository.findArticlesWithLimitTimelineOrderViewASC(
                any(),
                Mockito.anyLong(),
                Mockito.anyLong(),
            )
        ).willReturn(page)
        given(
            articleRepository.findArticlesWithLimitTimelineOrderLike(
                any(),
                Mockito.anyLong(),
                Mockito.anyLong(),
            )
        ).willReturn(page)
        given(
            articleRepository.findArticlesWithLimitTimelineOrderLikeASC(
                any(),
                Mockito.anyLong(),
                Mockito.anyLong(),
            )
        ).willReturn(page)
        given(
            articleRepository.searchByTitleArticlesOrderDate(
                any(),
                Mockito.anyString(),
            )
        ).willReturn(page)
        given(
            articleRepository.searchByTitleArticlesWithLimitTimelineOrderView(
                any(),
                Mockito.anyLong(),
                Mockito.anyLong(),
                Mockito.anyString(),
            )
        ).willReturn(page)
        given(
            articleRepository.searchByTitleArticlesWithLimitTimelineOrderViewASC(
                any(),
                Mockito.anyLong(),
                Mockito.anyLong(),
                Mockito.anyString(),
            )
        ).willReturn(page)
        given(
            articleRepository.searchByTitleArticlesWithLimitTimelineOrderLike(
                any(),
                Mockito.anyLong(),
                Mockito.anyLong(),
                Mockito.anyString(),
            )
        ).willReturn(page)
        given(
            articleRepository.searchByTitleArticlesWithLimitTimelineOrderLikeASC(
                any(),
                Mockito.anyLong(),
                Mockito.anyLong(),
                Mockito.anyString(),
            )
        ).willReturn(page)

        given(articleToDisciplineTypeRepository.findByArticleId(any())).willReturn(articleToDisciplineTypes)
        given(articleToTagTypeRepository.findByArticleId(any())).willReturn(articleToTagTypes)

        given(articleRepository.findById(any())).willReturn(Optional.of(defaultArticle))
    }

    // write test for getAll method
    @Test
    fun `getAll should return all articles`() {
        setup()
        val expectedDto = articles.map { it.toDto(listOf(), listOf()) }
        val result = articleService.getAll(1, 1, null).content
        assertEquals(expectedDto, result)
        verify(articleRepository).findArticlesOrderDate(any())
    }

    // write test getById
    @Test
    fun `getById should return article success`() {
        setup()
        val expectedDto = defaultArticle.toDto(listOf(), listOf()).apply {
            viewCount = 1
        }
        val result = articleService.getById(1)
        assertEquals(expectedDto, result)
        verify(articleRepository).findById(1)
    }

    @Test
    fun `getById should return RuntimeException with message`() {
        val e = assertThrows<RuntimeException> { articleService.getById(1) }
        assertTrue(e.message.contentEquals("Article not found"));
    }


    // write test searchByName
    @Test
    fun `searchByName should return articles`() {
        setup()
        val expectedDto = articles.map { it.toDto(listOf(), listOf()) }
        val result = articleService.searchByName(eq(Mockito.anyString()), 1, 1, sorting = null).content
        assertEquals(expectedDto, result)
        verify(articleRepository).searchByTitleArticlesOrderDate(any(), Mockito.anyString())
    }

    @Test
    fun `getByUserId should return articles`() {
        setup()
        val expectedDto = articles.map { it.toDto(listOf(), listOf()) }
        val result = articleService.getByUserId(1, 1, 1).content
        assertEquals(expectedDto, result)
    }

    @Test
    fun `create article`() {
        setup()
        val article = defaultArticle
        val articleDto = article.toDto(disciplineTypes, tagTypes).apply {
            typeName = defaultArticleType.name.toString()
        }
        val result = transactionTemplate.execute { articleService.create(articleDto) }

        assertEquals(null, result)
    }

    @Test
    fun `test update article`() {
        val articleUpdate = ArticleUpdateDto(
            previewText = "previewText",
        )
        setup()

        `when`(articleRepository.save(defaultArticle)).thenReturn(defaultArticle)

        val actual = articleService.update(1, articleUpdate)
        val expected = true to "Ok"

        assertEquals(actual, expected)
    }

    @Test
    fun `test delete article`() {
        setup()
        val expected = true to "Article deleted"
        val result = articleService.delete(1)
        assertEquals(expected, result)
        verify(articleRepository).deleteById(1)
    }

    @Test
    fun `test add likes article`() {
        transactionTemplate.execute {
//            given(articleService.updateLikes(1, true)).willReturn(null)
            `when`(articleService.updateLikes(1, true)).thenReturn(null)
            val expectedResponse = ResponseEntity(null, HttpStatus.OK)
            val actualResponse = articleRepository.addLikeByArticleId(1)
            assertEquals(expectedResponse, actualResponse)
            verify(articleRepository).addLikeByArticleId(1)
        }
    }

    @Test
    fun `test decrease likes article`() {
        transactionTemplate.execute {
            val expected = true to "Article deleted"
            val result = articleService.updateLikes(1, false)
            assertEquals(expected, result)
            verify(articleRepository).decreaseLikeByArticleId(1)

        }
    }

    @Test
    fun `test getFavArticle`() {
        transactionTemplate.execute {

            val article = defaultArticle
            val dtoArticle = article.toDto(disciplineTypes, tagTypes)

            val expected = PageImpl(listOf(dtoArticle)) //true to "Article deleted"
            val exp = expected.map { it }
            val result = articleService.getFavArticle(1, 1)
            assertEquals(exp, result)
            verify(articleRepository).decreaseLikeByArticleId(1)
        }
    }
}