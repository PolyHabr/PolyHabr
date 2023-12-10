package com.polyhabr.poly_back.unit.service

import com.polyhabr.poly_back.dto.FileCreatingDto
import com.polyhabr.poly_back.entity.*
import com.polyhabr.poly_back.entity.auth.Role
import com.polyhabr.poly_back.entity.auth.User
import com.polyhabr.poly_back.repository.ArticleRepository
import com.polyhabr.poly_back.repository.FileRepository
import com.polyhabr.poly_back.repository.auth.UsersRepository
import com.polyhabr.poly_back.service.impl.FileServiceImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.quality.Strictness
import org.springframework.data.domain.PageRequest
import java.util.*

@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FileServiceTest {

    @Mock
    private lateinit var fileRepository: FileRepository

    @Mock
    private lateinit var usersRepository: UsersRepository

    @Mock
    private lateinit var articleRepository: ArticleRepository

    @InjectMocks
    private lateinit var fileService: FileServiceImpl

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

        val defaultFile = File(
            id = "21313",
            username = defaultUser1.login,
            name = "name",
            type = "type",
            data = ByteArray(0)
        )

        val pageRequest = PageRequest.of(0, Int.MAX_VALUE)

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
    }

    @Test
    fun `test create success`() {
        val createObj = FileCreatingDto()

        given(usersRepository.findByLogin(null)).willReturn(ArticleServiceTest.defaultUser1)
        Mockito.`when`(fileRepository.save(any())).thenReturn(defaultFile)

        val actual = fileService.create(createObj, defaultFile.name)

        assertEquals(defaultFile, actual)
    }

    @Test
    fun `test create faild`() {
        val createObj = FileCreatingDto()

        val exception = assertThrows(IllegalStateException::class.java) {
            fileService.create(createObj, defaultFile.name)
        }

        val expectedMessage = "User not found"
        val actualMessage = exception.message!!

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    fun `test file to pdf success`() {
        val createObj = FileCreatingDto()

        val fileName = defaultFile.name

        given(usersRepository.findByLogin(null)).willReturn(ArticleServiceTest.defaultUser1)
        Mockito.`when`(articleRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(defaultArticle))
        Mockito.`when`(fileRepository.save(any())).thenReturn(defaultFile)

        val actual = fileService.createPdfForArticle(createObj, fileName, defaultArticle.id!!)

        assertEquals(defaultFile, actual)
    }

    @Test
    fun `test file to pdf faild 1`() {
        val createObj = FileCreatingDto()

        val fileName = defaultFile.name
        val expectedMessage = "Article not found"

        val exception = assertThrows(Exception::class.java) {
            fileService.createPdfForArticle(createObj, fileName, defaultArticle.id!!)
        }

        val actualMessage = exception.message!!

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    fun `test createPreviewPicForArticle success`() {
        val createObj = FileCreatingDto()

        val fileName = defaultFile.name

        given(usersRepository.findByLogin(null)).willReturn(ArticleServiceTest.defaultUser1)
        Mockito.`when`(articleRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(defaultArticle))
        Mockito.`when`(fileRepository.save(any())).thenReturn(defaultFile)

        val actual = fileService.createPreviewPicForArticle(createObj, fileName, defaultArticle.id!!)

        assertEquals(defaultFile, actual)
    }

    @Test
    fun `test createPreviewPicForArticle faild 1`() {
        val createObj = FileCreatingDto()

        val fileName = defaultFile.name
        val expectedMessage = "Article not found"

        val exception = assertThrows(Exception::class.java) {
            fileService.createPreviewPicForArticle(createObj, fileName, defaultArticle.id!!)
        }

        val actualMessage = exception.message!!

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    fun `test findById success`() {
        Mockito.`when`(fileRepository.findByIdWithNull(defaultFile.id!!)).thenReturn(defaultFile)

        val actual = fileService.findById(defaultFile.id!!)

        assertEquals(defaultFile, actual)
    }

    @Test
    fun `test delete success`() {
        given(usersRepository.findByLogin(null)).willReturn(ArticleServiceTest.defaultUser1)
        given(fileRepository.findByIdWithNull(defaultFile.id!!)).willReturn(defaultFile)
        Mockito.`when`(articleRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(defaultArticle))
        Mockito.`when`(fileRepository.save(any())).thenReturn(defaultFile)
        //Mockito.`when`(articleRepository.deleteById(Mockito.anyLong())).thenReturn(null)

        fileService.delete(defaultFile.id!!, defaultArticle.id!!)
    }

    @Test
    fun `test delete faild 1`() {
        val expectedMessage = "User not found"

        val exception = assertThrows(IllegalStateException::class.java) {
            fileService.delete(defaultFile.id!!, defaultArticle.id!!)
        }

        val actualMessage = exception.message!!

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    fun `test delete faild 2`() {
        val expectedMessage = "File not found"
        given(usersRepository.findByLogin(null)).willReturn(ArticleServiceTest.defaultUser1)

        val exception = assertThrows(IllegalStateException::class.java) {
            fileService.delete(defaultFile.id!!, defaultArticle.id!!)
        }

        val actualMessage = exception.message!!

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    fun `test delete faild 3`() {
        val expectedMessage = "Article not found"
        given(usersRepository.findByLogin(null)).willReturn(ArticleServiceTest.defaultUser1)
        given(fileRepository.findByIdWithNull(defaultFile.id!!)).willReturn(defaultFile)

        val exception = assertThrows(IllegalStateException::class.java) {
            fileService.delete(defaultFile.id!!, defaultArticle.id!!)
        }

        val actualMessage = exception.message!!

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    fun `test getRandomString`() {
        val expectedLength = 213213

        val actualLength = fileService.getRandomString(expectedLength).length

        assertEquals(expectedLength, actualLength)
    }
}