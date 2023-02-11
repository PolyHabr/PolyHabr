package com.polyhabr.poly_back.controller.model.user.response

import com.polyhabr.poly_back.dto.UserDto

data class UserListResponse(
    val users: List<UserResponse>,
    val page: Int,
)

fun List<UserDto>.toResponse(): UserListResponse {
    return UserListResponse(
        users = this.map { it.toResponse() },
        page = 0, // todo
    )
}
