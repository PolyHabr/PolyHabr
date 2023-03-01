package com.polyhabr.poly_back.controller.model.user.response

import com.polyhabr.poly_back.dto.UserDto

data class UserMeResponse(
    val id: Long,
    val email: String,
    val login: String,
    val name: String,
    val surname: String,
)

fun UserDto.toMeResponse(): UserMeResponse {
    return UserMeResponse(
        id = this.id!!,
        email = this.email!!,
        login = this.login!!,
        name = this.name!!,
        surname = this.surname!!,
    )
}
