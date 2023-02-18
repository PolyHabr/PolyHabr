package com.polyhabr.poly_back.service

import com.polyhabr.poly_back.controller.model.user.request.UserRequest
import com.polyhabr.poly_back.dto.UserDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort

interface UsersService {
    fun getAll(offset: Int, size: Int): Page<UserDto>

    fun getById(id: Long): UserDto

    fun search(prefix: String, offset: Int, size: Int): List<UserDto>

    fun create(userRequest: UserRequest): Long?

    fun update(id: Long, userRequest: UserRequest): Boolean

    fun delete(id: Long)
//    abstract fun <Users> Users(id: Long, email: String, login: String, name: String, password: String, surname: String): Users
}