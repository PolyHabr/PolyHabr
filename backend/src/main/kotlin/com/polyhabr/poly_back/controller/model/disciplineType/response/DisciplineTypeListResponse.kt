package com.polyhabr.poly_back.controller.model.disciplineType.response

import com.polyhabr.poly_back.dto.DisciplineTypeDto
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.Page

data class DisciplineTypeListResponse(
    @field:Schema(description = "List of discipline types")
    val contents: List<DisciplineTypeResponse>,
    @field:Schema(description = "Total number of elements")
    val totalElements: Long,
    @field:Schema(description = "Total number of pages")
    val totalPages: Int,
)

fun Page<DisciplineTypeDto>.toListResponse(): DisciplineTypeListResponse {
    return DisciplineTypeListResponse(
        contents = this.content.map { it.toResponse() },
        totalElements = this.totalElements,
        totalPages = this.totalPages,
    )
}