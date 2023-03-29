package com.polyhabr.poly_back.unit.controller

import com.polyhabr.poly_back.controller.DisciplineTypeController
import com.polyhabr.poly_back.controller.model.disciplineType.request.DisciplineTypeRequest
import com.polyhabr.poly_back.controller.model.disciplineType.request.UpdateMyDisciplineRequest
import com.polyhabr.poly_back.controller.model.disciplineType.response.*
import com.polyhabr.poly_back.controller.utils.SimpleSuccessResponse
import com.polyhabr.poly_back.entity.DisciplineType
import com.polyhabr.poly_back.entity.toDto
import com.polyhabr.poly_back.service.DisciplineTypeService
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
class DisciplineTypeControllerTest {

    companion object {
        const val DEFAULT_PAGE = 1
        const val DEFAULT_SIZE = 1

        val defaultDisciplineType = DisciplineType(
            name = "discipline",
        ).apply {
            id = 1L
        }
    }

    @Mock
    private lateinit var disciplineTypeService: DisciplineTypeService

    @InjectMocks
    private lateinit var disciplineTypeController: DisciplineTypeController

    // write test get all discipline types
    @Test
    fun `test get all discipline types`() {
        val list = listOf(defaultDisciplineType)
        val dtoList = list.map { it.toDto() }
        val page = PageImpl(dtoList)

        given(disciplineTypeService.getAll(DEFAULT_PAGE, DEFAULT_SIZE)).willReturn(page)

        val expectedResponse = ResponseEntity.ok(page.toListResponse())
        val actualResponse = disciplineTypeController.getAll(DEFAULT_PAGE, DEFAULT_SIZE)

        assertEquals(expectedResponse, actualResponse)
    }

    // write test get my discipline types
    @Test
    fun `test get my discipline types`() {
        val list = listOf(defaultDisciplineType)
        val dtoList = list.map { it.toDto() }

        given(disciplineTypeService.getMy()).willReturn(dtoList)

        val expectedResponse = ResponseEntity.ok(DisciplineTypeSimpleListResponse(
            disciplineTypeService.getMy().map { it.toResponse() }
        ))
        val actualResponse = disciplineTypeController.getMy()

        assertEquals(expectedResponse, actualResponse)
    }

    // write test update my discipline type
    @Test
    fun `test update my discipline type`() {
        val name = "discipline"
        val updateResponse = UpdateMyDisciplineRequest(listOf(name))

        given(disciplineTypeService.updateMy(updateResponse)).willReturn(true)

        val expectedResponse = ResponseEntity.ok(SimpleSuccessResponse(true))
        val actualResponse = disciplineTypeController.updateMyDiscipline(updateResponse)

        assertEquals(expectedResponse, actualResponse)
    }

    // write test search discipline types by name
    @Test
    fun `test search discipline types by name`() {
        val list = listOf(defaultDisciplineType)
        val dtoList = list.map { it.toDto() }
        val page = PageImpl(dtoList)

        given(disciplineTypeService.searchByName("discipline", DEFAULT_PAGE, DEFAULT_SIZE)).willReturn(page)

        val expectedResponse = ResponseEntity.ok(page.toListResponse())
        val actualResponse =
            disciplineTypeController.searchDisciplineTypesByName("discipline", DEFAULT_PAGE, DEFAULT_SIZE)

        assertEquals(expectedResponse, actualResponse)
    }

    // write test create discipline type
    @Test
    fun `test create discipline type`() {
        val name = "discipline"
        val disciplineTypeRequest = DisciplineTypeRequest(name)

        given(disciplineTypeService.create(disciplineTypeRequest)).willReturn(1L)

        val expectedResponse = ResponseEntity.ok(DisciplineTypeCreateResponse(id = 1L, isSuccess = true))
        val actualResponse = disciplineTypeController.create(disciplineTypeRequest)

        assertEquals(expectedResponse, actualResponse)
    }

    // write test update discipline type
    @Test
    fun `test update discipline type`() {
        val name = "discipline"
        val disciplineTypeRequest = DisciplineTypeRequest(name)

        given(disciplineTypeService.update(1L, disciplineTypeRequest)).willReturn(Pair(true, "success"))

        val expectedResponse = ResponseEntity.ok(DisciplineTypeUpdateResponse(isSuccess = true, message = "success"))
        val actualResponse = disciplineTypeController.update(1L, disciplineTypeRequest)

        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `test get discipline types by id`() {
        val disciplineType = defaultDisciplineType
        val dtoDisciplineType = disciplineType.toDto()

        given(disciplineTypeService.getById(1)).willReturn(dtoDisciplineType)

        val expectedResponse = ResponseEntity.ok(DisciplineTypeResponse( dtoDisciplineType.name))
        val actualResponse = disciplineTypeController.getById(1)

        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `test delete discipline types`() {
        val disciplineType = defaultDisciplineType
        val dtoDisciplineType = disciplineType.toDto()
        val expected = true to "success"

        given(disciplineTypeService.delete(1)).willReturn(expected)

        val expectedResponse = ResponseEntity(null, HttpStatus.OK)

        val actualResponse = disciplineTypeController.delete(1)

        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `test handleConstraintViolationException`() {
        val exString = "exString"
        val ex = ConstraintViolationException(exString, null)

        val actual = disciplineTypeController.handleConstraintViolationException(ex)!!
        val expectedBody = "not valid due to validation error: " + ex.message

        assertEquals(actual.statusCode, HttpStatus.BAD_REQUEST)
        assertEquals(actual.body, expectedBody)
    }
}