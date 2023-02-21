package com.polyhabr.poly_back.controller.model.comment.response

import com.polyhabr.poly_back.dto.CommentDto
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.Page

data class CommentListResponse (
    @field:Schema(description = "List of comments")
    val contents: List<CommentResponse>,
    @field:Schema(description = "Total number of elements")
    val totalElements: Long,
    @field:Schema(description = "Total number of pages")
    val totalPages: Int,
)

fun Page<CommentDto>.toListResponse(): CommentListResponse{
    return CommentListResponse(
        contents = this.content.map { it.toResponse() },
        totalElements = this.totalElements,
        totalPages = this.totalPages,
    )
}