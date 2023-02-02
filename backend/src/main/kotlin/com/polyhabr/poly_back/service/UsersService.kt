package com.polyhabr.poly_back.service

import com.polyhabr.poly_back.dto.UsersDto

interface UsersService {
    fun getAll(): List<UsersDto>

}