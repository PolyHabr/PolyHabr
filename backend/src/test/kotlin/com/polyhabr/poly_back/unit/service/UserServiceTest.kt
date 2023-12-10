package com.polyhabr.poly_back.unit.service

import com.polyhabr.poly_back.controller.model.user.request.UserRequest
import com.polyhabr.poly_back.controller.model.user.request.UserUpdateRequest
import com.polyhabr.poly_back.entity.auth.Role
import com.polyhabr.poly_back.entity.auth.User
import com.polyhabr.poly_back.entity.auth.toDto
import com.polyhabr.poly_back.entity.auth.toDtoWithoutPasswordAndEmail
import com.polyhabr.poly_back.repository.auth.UsersRepository
import com.polyhabr.poly_back.service.impl.UsersServiceImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

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
    }

    @Mock
    lateinit var userRepository: UsersRepository

    @InjectMocks
    lateinit var userService: UsersServiceImpl

    @Test
    fun `test get user by id`() {
        val id = defaultUser1.id!!
        val user = defaultUser1
        val userDto = defaultUser1.toDto()

        Mockito.`when`(userRepository.findById(id)).thenReturn(Optional.of(user))

        val result = userService.getById(id)

        assertEquals(userDto, result)
    }

    @Test
    fun `test get all users`() {
        val users = listOf(defaultUser1, defaultUser2)
        val usersDto = users.map { it.toDto() }
        val page = PageImpl(users)

        Mockito.`when`(userRepository.findAll(Mockito.any(PageRequest::class.java))).thenReturn(page)

        val result = userService.getAll(pageRequest.pageNumber, pageRequest.pageSize)
            .content

        assertEquals(usersDto, result)
    }

    // write test for update user
    @Test
    fun `test update user`() {
        val user = defaultUser1
        val newName = user.name + "1"
        val updateRequest = UserUpdateRequest(
            name = newName
        )
        val userEdited = User(
            login = user.login,
            password = user.password,
            name = newName,
            surname = user.surname,
            email = user.email,
            enabled = user.enabled,
            roles = user.roles
        ).apply {
            id = user.id
        }

        Mockito.`when`(userRepository.save(user)).thenReturn(userEdited)
        Mockito.`when`(userRepository.findByLogin(null)).thenReturn(user)

        val result = userService.update(updateRequest)

        assertEquals(true, result.first)
    }

    // write test for getMyUser
    @Test
    fun `test get my user`() {
        val user = defaultUser1
        val userDto = user.toDto()

        Mockito.`when`(userRepository.findByLogin(null)).thenReturn(user)

        val result = userService.getMyUser()

        assertEquals(userDto, result)
    }

    // write test for searchByName
    @Test
    fun `test search by name`() {
        val user = defaultUser1
        val userDto = user.toDtoWithoutPasswordAndEmail()
        val page = PageImpl(listOf(user))

        given(userRepository.findUsersByName(any(), any())).willReturn(page)

        val result = userService.searchByName(user.name, 1, 1)
            .content

        assertEquals(listOf(userDto), result)
    }

    // write test for create user
    @Test
    fun `test create user`() {
        val user = defaultUser1
        val userRequest = UserRequest(
            login = user.login,
            password = user.password,
            name = user.name,
            surname = user.surname,
            email = user.email,
            id = 1L,
        )
        val createdUser = defaultUser1

        given(userRepository.save(Mockito.any(User::class.java))).willReturn(createdUser)
        val result = userService.create(userRequest)

        assertNotNull(result)
    }

    // write test for delete user
    @Test
    fun `test delete user`() {
        val user = defaultUser1

        given(userRepository.findByLogin(null)).willReturn(user)
        val result = userService.delete()

        assertEquals(true, result.first)
    }

    // write test for verify
    @Test
    fun `test verify`() {
        val user = defaultUser2

        given(userRepository.findByVerificationCode(any())).willReturn(user)
        given(userRepository.save(Mockito.any(User::class.java))).willReturn(user)
        val result = userService.verify(Mockito.anyString())

        assert(result)
    }
}