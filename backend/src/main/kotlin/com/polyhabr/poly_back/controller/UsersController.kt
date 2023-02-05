package com.polyhabr.poly_back.controller

import com.polyhabr.poly_back.dto.UserDto
import com.polyhabr.poly_back.service.UsersService
import org.springframework.web.bind.annotation.*
import com.polyhabr.poly_back.controller.model.UserModel

@RestController
@RequestMapping("/users")
class UsersController(
    private val usersService: UsersService,
) {
    @GetMapping
    fun getAll(): List<UserDto> = usersService.getAll()

    @GetMapping("/byId")
    fun getById(@RequestParam("id") id: Long): UserDto =
        usersService.getById(id)

    @GetMapping("/search")
    fun searchUsers(@RequestParam("prefix") prefix: String): List<UserDto> =
        usersService.search(prefix)

    @PostMapping("/create")
    fun create(@RequestBody user: UserModel): Boolean {
        return usersService.create(user) != null
    }

    @PutMapping("/update")
    fun update(@RequestParam("id") id: Long, @RequestBody user: UserModel) {
        usersService.update(id, user)
    }

    @DeleteMapping("/delete")
    fun delete(@RequestParam(value = "id") id: Long) {
        usersService.delete(id)
    }
}