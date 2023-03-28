package com.polyhabr.poly_back.controller

import com.polyhabr.poly_back.controller.model.tagType.request.TagTypeRequest
import com.polyhabr.poly_back.controller.model.tagType.response.TagTypeCreateResponse
import com.polyhabr.poly_back.controller.model.tagType.response.TagTypeUpdateResponse
import com.polyhabr.poly_back.controller.model.tagType.response.toListResponse
import com.polyhabr.poly_back.controller.model.tagType.response.toResponse
import com.polyhabr.poly_back.entity.TagType
import com.polyhabr.poly_back.entity.toDto
import com.polyhabr.poly_back.service.TagTypeService
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
class TagTypeControllerTest {
    companion object {
        const val DEFAULT_PAGE = 1
        const val DEFAULT_SIZE = 1

        val defaultTagType = TagType(
            name = "tag",
        ).apply {
            id = 1L
        }
    }

    @Mock
    private lateinit var tagTypeService: TagTypeService

    @InjectMocks
    private lateinit var tagTypeController: TagTypeController

    // write test get all tag types
    @Test
    fun `test get all tag types`() {
        val tagType = defaultTagType
        val dtoTagType = tagType.toDto()
        val listOf = listOf(dtoTagType)
        val page = PageImpl(listOf)

        given(tagTypeService.getAll(DEFAULT_PAGE, DEFAULT_SIZE)).willReturn(page)

        val expectedResponse = ResponseEntity.ok(page.toListResponse())
        val actualResponse = tagTypeController.getAll(DEFAULT_PAGE, DEFAULT_SIZE)

        assertEquals(expectedResponse, actualResponse)
    }

    // write test get tag types by id
    @Test
    fun `test get tag types by id`() {
        val tagType = defaultTagType
        val dtoTagType = tagType.toDto()

        given(tagTypeService.getById(tagType.id!!)).willReturn(dtoTagType)

        val expectedResponse = ResponseEntity.ok(dtoTagType.toResponse())
        val actualResponse = tagTypeController.getById(tagType.id!!)

        assertEquals(expectedResponse, actualResponse)
    }

    // write test create tag types
    @Test
    fun `test create tag types`() {
        val tagType = defaultTagType
        val dtoTagTypeRequest = TagTypeRequest(
            name = tagType.name
        )

        given(tagTypeService.create(dtoTagTypeRequest)).willReturn(tagType.id!!)

        val expectedResponse = ResponseEntity.ok(TagTypeCreateResponse(id = tagType.id!!, isSuccess = true))
        val actualResponse = tagTypeController.create(dtoTagTypeRequest)

        assertEquals(expectedResponse, actualResponse)
    }

    // write test update tag types
    @Test
    fun `test update tag types`() {
        val tagType = defaultTagType
        val dtoTagTypeRequest = TagTypeRequest(
            name = tagType.name
        )

        given(tagTypeService.update(tagType.id!!, dtoTagTypeRequest)).willReturn(true to "success")

        val expectedResponse = ResponseEntity.ok(TagTypeUpdateResponse(message = "success", isSuccess = true))
        val actualResponse = tagTypeController.update(tagType.id!!, dtoTagTypeRequest)

        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `test search TagType by name`() {
        val tagType = defaultTagType
        val dtoTagType = tagType.toDto()
        val listOf = listOf(dtoTagType)
        val page = PageImpl(listOf)

        given(tagTypeService.searchByName("tag", 1, 1)).willReturn(page)

        val expectedResponse = ResponseEntity(page.toListResponse(), HttpStatus.OK)
        val actualResponse = tagTypeController.searchTagTypesByName("tag", 1, 1)

        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `test delete tagType`() {
        val tagType = defaultTagType
        val dtoTagType = tagType.toDto()
        val expected = true to "success"

        given(tagTypeService.delete(1)).willReturn(expected)

        val expectedResponse = ResponseEntity("success", HttpStatus.OK)

        val actualResponse = tagTypeController.delete(1)

        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `test delete non-existing tagType`() {
        val expected = false to "INTERNAL_SERVER_ERROR Internal Server Error"

        given(tagTypeService.delete(1)).willReturn(expected)
        val expectedResponse = ResponseEntity("INTERNAL_SERVER_ERROR Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR)

        val actualResponse = tagTypeController.delete(1)
        assertEquals(expectedResponse, actualResponse)

    }

    @Test
    fun `test handleConstraintViolationException`() {
        val exString = "exString"
        val ex = ConstraintViolationException(exString, null)

        val actual = tagTypeController.handleConstraintViolationException(ex)!!
        val expectedBody = "not valid due to validation error: " + ex.message

        assertEquals(actual.statusCode, HttpStatus.BAD_REQUEST)
        assertEquals(actual.body, expectedBody)
    }
}