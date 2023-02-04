package com.polyhabr.poly_back.controller

import com.polyhabr.poly_back.dto.UsersDto
import com.polyhabr.poly_back.service.UsersService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UsersController(
    private val usersService: UsersService,
) {
    @GetMapping
    fun getAll(): List<UsersDto> = usersService.getAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: Int): UsersDto =
        usersService.getById(id)

    @GetMapping("/search")
    fun searchUsers(@RequestParam("prefix") prefix: String): List<UsersDto> =
        usersService.search(prefix)

    @PostMapping
    fun create(@RequestBody dto: UsersDto): Int{
        return usersService.create(dto)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Int, @RequestBody dto: UsersDto){
        usersService.update(id, dto)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int){
        usersService.delete(id)
    }

}