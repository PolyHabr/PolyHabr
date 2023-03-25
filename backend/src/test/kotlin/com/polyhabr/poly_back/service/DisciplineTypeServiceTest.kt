package com.polyhabr.poly_back.service

import com.polyhabr.poly_back.controller.model.disciplineType.request.DisciplineTypeRequest
import com.polyhabr.poly_back.entity.DisciplineType
import com.polyhabr.poly_back.entity.auth.Role
import com.polyhabr.poly_back.entity.auth.User
import com.polyhabr.poly_back.entity.toDto
import com.polyhabr.poly_back.repository.DisciplineTypeRepository
import com.polyhabr.poly_back.repository.MyDisciplineRepository
import com.polyhabr.poly_back.repository.auth.UsersRepository
import com.polyhabr.poly_back.service.impl.DisciplineTypeServiceImpl
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

class DisciplineTypeServiceTest {
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

        val defaultDisciplineType1 = DisciplineType(
            name = "DisciplineType1",
        ).apply {
            id = 1L
        }

        val defaultDisciplineType2 = DisciplineType(
            name = "DisciplineType2",
        ).apply {
            id = 2L
        }
    }

    @Mock
    private lateinit var disciplineTypeRepository: DisciplineTypeRepository

    @Mock
    private lateinit var usersRepository: UsersRepository

    @Mock
    private lateinit var myDisciplineRepository: MyDisciplineRepository

    @InjectMocks
    private lateinit var disciplineTypeService: DisciplineTypeServiceImpl

    @Test
    fun `get all discipline types`() {
        val disciplineTypes = listOf(defaultDisciplineType1, defaultDisciplineType2)
        val expectedDiscipline = disciplineTypes.map { it.toDto() }
        val page = PageImpl(disciplineTypes)

        given(disciplineTypeRepository.findAll(Mockito.any(PageRequest::class.java))).willReturn(page)

        val result = disciplineTypeService.getAll(1, 1)
            .content

        Assertions.assertEquals(expectedDiscipline, result)
    }

    @Test
    fun `get discipline type by id`() {
        val disciplineType = defaultDisciplineType1
        val expectedDiscipline = disciplineType.toDto()

        given(disciplineTypeRepository.findById(any())).willReturn(Optional.of(disciplineType))

        val result = disciplineTypeService.getById(1L)

        Assertions.assertEquals(expectedDiscipline, result)
    }

    @Test
    fun `search by name discipline type`() {
        val disciplineTypes = listOf(defaultDisciplineType1, defaultDisciplineType2)
        val expectedDiscipline = disciplineTypes.map { it.toDto() }
        val page = PageImpl(disciplineTypes)
        val prefix = "disciplineType"

        given(disciplineTypeRepository.findDisciplineTypesByName(any(), eq(prefix))).willReturn(page)

        val result = disciplineTypeService.searchByName(prefix, 1, 1)
            .content

        Assertions.assertEquals(expectedDiscipline, result)
    }

    @Test
    fun `create discipline type`() {
        val disciplineType = defaultDisciplineType1
        val disciplineTypeRequest = DisciplineTypeRequest(
            name = "disciplineType1",
        )

        given(disciplineTypeRepository.save(Mockito.any(DisciplineType::class.java))).willReturn(disciplineType)

        val result = disciplineTypeService.create(disciplineTypeRequest)

        Assertions.assertNotNull(result)
    }

    @Test
    fun `update discipline type`() {
        val disciplineType = DisciplineTypeServiceTest.defaultDisciplineType1
        val disciplineTypeRequest = DisciplineTypeRequest(
            name = "disciplineType1",
        )

        given(disciplineTypeRepository.findById(any())).willReturn(Optional.of(disciplineType))
        given(disciplineTypeRepository.save(Mockito.any(DisciplineType::class.java))).willReturn(disciplineType)

        val result = disciplineTypeService.update(1L, disciplineTypeRequest)

        Assertions.assertNotNull(result)
    }
}