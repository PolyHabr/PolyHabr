package com.polyhabr.poly_back.controller.model.user.response

import com.polyhabr.poly_back.controller.validator.UserValidator
import com.polyhabr.poly_back.dto.UserDto

data class UserResponse(
    val id: Long,
    val email: String,
    val login: String,
    val password: String,
    val name: String,
    val surname: String,
)

fun UserDto.toResponse(): UserResponse {
    val res = UserValidator.validate(this)
    if (!res) throw IllegalArgumentException("UserDto is not valid")
    return UserResponse(
        id = this.id!!,
        email = this.email!!,
        login = this.login!!,
        name = this.name!!,
        password = this.password!!,
        surname = this.surname!!,
    )
}
