package com.polyhabr.poly_back.service.impl

import com.polyhabr.poly_back.dto.UsersDto
import com.polyhabr.poly_back.service.UsersService
import org.springframework.stereotype.Service

@Service
class UsersServiceImpl : UsersService {
    override fun getAll(): List<UsersDto> {
        return listOf(
            UsersDto(1, "aaaa@gmail.com", "aaaa", "aaaa", "aaaa", "aaaa"),
            UsersDto(2, "bbbb@gmail.com", "bbbb", "bbbb", "bbbb", "bbbb"),
            UsersDto(3, "cccc@gmail.com", "cccc", "cccc", "cccc", "cccc"),
            UsersDto(4, "dddd@gmail.com", "dddd", "dddd", "dddd", "dddd"),
        )
    }
}