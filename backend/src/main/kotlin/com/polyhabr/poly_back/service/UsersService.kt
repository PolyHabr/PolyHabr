package com.polyhabr.poly_back.service

import com.polyhabr.poly_back.controller.model.user.request.UserRequest
import com.polyhabr.poly_back.dto.UserDto
import com.polyhabr.poly_back.entity.User

interface UsersService {
    fun getAll(): List<UserDto>

    fun getById(id: Long): UserDto

    fun search(prefix: String): List<UserDto>

    fun create(userRequest: UserRequest): Long?

    fun update(id: Long, userRequest: UserRequest): Boolean

    fun delete(id: Long)
//    abstract fun <Users> Users(id: Long, email: String, login: String, name: String, password: String, surname: String): Users
}