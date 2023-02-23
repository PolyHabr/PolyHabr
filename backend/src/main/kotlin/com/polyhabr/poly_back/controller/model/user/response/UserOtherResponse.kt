package com.polyhabr.poly_back.controller.model.user.response

import com.polyhabr.poly_back.dto.UserDto

data class UserOtherResponse(
    val id: Long,
    val login: String,
    val name: String,
    val surname: String,
)

fun UserDto.toOtherResponse(): UserOtherResponse {
    return UserOtherResponse(
        id = this.id!!,
        login = this.login!!,
        name = this.name!!,
        surname = this.surname!!,
    )
}
