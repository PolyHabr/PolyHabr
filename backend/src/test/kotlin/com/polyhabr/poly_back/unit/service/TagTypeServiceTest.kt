package com.polyhabr.poly_back.unit.service

import com.polyhabr.poly_back.controller.model.tagType.request.TagTypeRequest
import com.polyhabr.poly_back.entity.TagType
import com.polyhabr.poly_back.entity.auth.Role
import com.polyhabr.poly_back.entity.auth.User
import com.polyhabr.poly_back.entity.toDto
import com.polyhabr.poly_back.repository.TagTypeRepository
import com.polyhabr.poly_back.service.impl.TagTypeServiceImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*

@ExtendWith(MockitoExtension::class)
class TagTypeServiceTest {
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

        val defaultTagType1 = TagType(
            name = "tagType1",
        ).apply {
            id = 1L
        }

        val defaultTagType2 = TagType(
            name = "tagType2",
        ).apply {
            id = 2L
        }
    }

    @Mock
    private lateinit var tagTypeRepository: TagTypeRepository

    @InjectMocks
    private lateinit var tagTypeService: TagTypeServiceImpl

    // write test get all tag types
    @Test
    fun `get all tag types`() {
        val tagTypes = listOf(defaultTagType1, defaultTagType2)
        val expectedTags = tagTypes.map { it.toDto() }
        val page = PageImpl(tagTypes)

        given(tagTypeRepository.findAll(Mockito.any(PageRequest::class.java))).willReturn(page)

        val result = tagTypeService.getAll(1, 1)
            .content

        assertEquals(expectedTags, result)
    }

    // write test get tag type by id
    @Test
    fun `get tag type by id`() {
        val tagType = defaultTagType1
        val expectedTag = tagType.toDto()

        given(tagTypeRepository.findById(any())).willReturn(Optional.of(tagType))

        val result = tagTypeService.getById(1L)

        assertEquals(expectedTag, result)
    }

    @Test
    fun `get tag type by id faild`() {
        val tagType = defaultTagType1

        val exception = Assertions.assertThrows(RuntimeException::class.java) {
            tagTypeService.getById(tagType.id!!)
        }

        val expectedMessage = "Tag type not found"
        val actualMessage = exception.message!!

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    // write test search by name tag type
    @Test
    fun `search by name tag type`() {
        val tagTypes = listOf(defaultTagType1, defaultTagType2)
        val expectedTags = tagTypes.map { it.toDto() }
        val page = PageImpl(tagTypes)
        val prefix = "tagType"

        given(tagTypeRepository.findTagTypesByName(any(), eq(prefix))).willReturn(page)

        val result = tagTypeService.searchByName(prefix, 1, 1)
            .content

        assertEquals(expectedTags, result)
    }

    // write test create tag type
    @Test
    fun `create tag type`() {
        val tagType = defaultTagType1
        val tagTypeRequest = TagTypeRequest(
            name = "tagType1",
        )

        given(tagTypeRepository.save(Mockito.any(TagType::class.java))).willReturn(tagType)

        val result = tagTypeService.create(tagTypeRequest)

        assertNotNull(result)
    }

    // write test update tag type
    @Test
    fun `update tag type`() {
        val tagType = defaultTagType1
        val tagTypeRequest = TagTypeRequest(
            name = "tagType1",
        )

        given(tagTypeRepository.findById(any())).willReturn(Optional.of(tagType))
        given(tagTypeRepository.save(Mockito.any(TagType::class.java))).willReturn(tagType)

        val result = tagTypeService.update(1L, tagTypeRequest)

        assertNotNull(result)
    }

    @Test
    fun `delete tagType`() {
        val result = tagTypeService.delete(1L)

        Assertions.assertNotNull(result)
    }
}