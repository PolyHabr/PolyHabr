package com.polyhabr.poly_back.service

import com.polyhabr.poly_back.dto.UsersDto

interface UsersService {
    fun getAll(): List<UsersDto>

    fun getById(id: Int): UsersDto

    fun search(prefix: String): List<UsersDto>

    fun create(dto: UsersDto): Int

    fun update(id: Int, dto: UsersDto)

    fun delete(id: Int)
//    abstract fun <Users> Users(id: Int, email: String, login: String, name: String, password: String, surname: String): Users
}