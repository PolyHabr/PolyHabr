package com.polyhabr.poly_back.controller

import com.polyhabr.poly_back.dto.UsersDto
import com.polyhabr.poly_back.service.UsersService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UsersController(
    private val usersService: UsersService,
) {
    @GetMapping
    fun getAll(): List<UsersDto> = usersService.getAll()
}