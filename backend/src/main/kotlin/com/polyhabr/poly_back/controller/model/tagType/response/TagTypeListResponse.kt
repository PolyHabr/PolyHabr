package com.polyhabr.poly_back.controller.model.tagType.response

import com.polyhabr.poly_back.dto.TagTypeDto
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.Page

data class TagTypeListResponse(
    @field:Schema(description = "List of tag types")
    val contents: List<TagTypeResponse>,
    @field:Schema(description = "Total number of elements")
    val totalElements: Long,
    @field:Schema(description = "Total number of pages")
    val totalPages: Int,
)

fun Page<TagTypeDto>.toListResponse(): TagTypeListResponse{
    return TagTypeListResponse(
        contents = this.content.map { it.toResponse() },
        totalElements = this.totalElements,
        totalPages = this.totalPages,
    )
}