package com.polyhabr.poly_back.service

import com.polyhabr.poly_back.controller.model.UserModel
import com.polyhabr.poly_back.dto.UserDto

interface UsersService {
    fun getAll(): List<UserDto>

    fun getById(id: Long): UserDto

    fun search(prefix: String): List<UserDto>

    fun create(userModel: UserModel): Long?

    fun update(id: Long, userModel: UserModel)

    fun delete(id: Long)
//    abstract fun <Users> Users(id: Long, email: String, login: String, name: String, password: String, surname: String): Users
}