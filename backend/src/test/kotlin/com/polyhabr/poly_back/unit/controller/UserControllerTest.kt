package com.polyhabr.poly_back.unit.controller

import com.polyhabr.poly_back.controller.UsersController
import com.polyhabr.poly_back.controller.model.user.request.UserUpdateRequest
import com.polyhabr.poly_back.controller.model.user.response.*
import com.polyhabr.poly_back.entity.auth.Role
import com.polyhabr.poly_back.entity.auth.User
import com.polyhabr.poly_back.entity.auth.toDto
import com.polyhabr.poly_back.entity.auth.toDtoWithoutPasswordAndEmail
import com.polyhabr.poly_back.repository.auth.UsersRepository
import com.polyhabr.poly_back.service.UsersService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import javax.validation.ConstraintViolationException


@ExtendWith(MockitoExtension::class)
class UserControllerTest {

    private companion object {
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
    }

    @Mock
    private lateinit var userService: UsersService

    @Mock
    private lateinit var usersRepository: UsersRepository

    @Mock
    private lateinit var userDetailsService: UserDetailsService

    @Mock
    private lateinit var userDetails: UserDetails

    @InjectMocks
    private lateinit var userController: UsersController

    // write test get all users
    @Test
    fun `test get all user`() {
        val user = defaultUser1
        val userDto = user.toDtoWithoutPasswordAndEmail()
        val page = PageImpl(listOf(userDto))

        given(userService.getAll(1, 1)).willReturn(page)

        val expectedResponse = ResponseEntity(page.toListResponse(), HttpStatus.OK)
        val actualResponse = userController.getAll(1, 1)

        assertEquals(expectedResponse, actualResponse)
    }

    // write test get user by id
    @Test
    fun `test get user by id`() {
        val user = defaultUser1
        val userDto = user.toDtoWithoutPasswordAndEmail()

        given(userService.getById(1)).willReturn(userDto)

        val expectedResponse = ResponseEntity(userDto.toOtherResponse(), HttpStatus.OK)
        val actualResponse = userController.getById(1)

        assertEquals(expectedResponse, actualResponse)
    }

    // write test my user
    @Test
    fun `test get my user`() {
        val user = defaultUser1
        val userDto = user.toDto()

        given(userService.getMyUser()).willReturn(userDto)

        val expectedResponse = ResponseEntity(userDto.toMeResponse(), HttpStatus.OK)
        val actualResponse = userController.getMyUser()

        assertEquals(expectedResponse, actualResponse)
    }

    // write search user by name
    @Test
    fun `test search user by name`() {
        val user = defaultUser1
        val userDto = user.toDtoWithoutPasswordAndEmail()
        val page = PageImpl(listOf(userDto))

        given(userService.searchByName("dmitry", 1, 1)).willReturn(page)

        val expectedResponse = ResponseEntity(page.toListResponse(), HttpStatus.OK)
        val actualResponse = userController.searchUsersByName("dmitry", 1, 1)

        assertEquals(expectedResponse, actualResponse)
    }

    // write test update user
    @Test
    fun `test update user`() {
        val userUpdateRequest = UserUpdateRequest(
            name = "dmit213123213ry",
            surname = "21312312321"
        )

        given(userService.update(any())).willReturn(Pair(true, "success"))

        val expectedResponse = ResponseEntity(UserUpdateResponse(true, "success"), HttpStatus.OK)
        val actualResponse = userController.update(userUpdateRequest)

        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `test delete user`() {
        val user = defaultUser1
        val userDto = user.toDtoWithoutPasswordAndEmail()
        val expected = true to "success"

        given(userService.delete()).willReturn(expected)

        val expectedResponse = ResponseEntity("success", HttpStatus.OK)

        val actualResponse = userController.delete()

        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `test delete non-existing user`() {
        val expected = false to "INTERNAL_SERVER_ERROR Internal Server Error"

        given(userService.delete()).willReturn(expected)
        val expectedResponse =
            ResponseEntity("INTERNAL_SERVER_ERROR Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR)

        val actualResponse = userController.delete()
        assertEquals(expectedResponse, actualResponse)

    }

    @Test
    fun `test handleConstraintViolationException`() {
        val exString = "exString"
        val ex = ConstraintViolationException(exString, null)

        val actual = userController.handleConstraintViolationException(ex)!!
        val expectedBody = "not valid due to validation error: " + ex.message

        assertEquals(actual.statusCode, HttpStatus.BAD_REQUEST)
        assertEquals(actual.body, expectedBody)
    }

}