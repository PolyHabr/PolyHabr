package com.polyhabr.poly_back.controller.model.user.response

import com.polyhabr.poly_back.dto.UserDto
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.Page

data class UserListResponse(
    @field:Schema(description = "List of users")
    val contents: List<UserResponse>,
    @field:Schema(description = "Total number of elements")
    val totalElements: Long,
    @field:Schema(description = "Total number of pages")
    val totalPages: Int,
)

fun Page<UserDto>.toListResponse(): UserListResponse {
    return UserListResponse(
        contents = this.content.map { it.toResponse() },
        totalElements = this.totalElements,
        totalPages = this.totalPages,
    )
}
