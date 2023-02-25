package com.polyhabr.poly_back.controller.model.articleType.response

import com.polyhabr.poly_back.dto.ArticleTypeDto
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.Page

data class ArticleTypeListResponse(
    @field:Schema(description = "List of article types")
    val contents: List<ArticleTypeResponse>,
    @field:Schema(description = "Total number of elements")
    val totalElements: Long,
    @field:Schema(description = "Total number of pages")
    val totalPages: Int,
)

fun Page<ArticleTypeDto>.toListResponse(): ArticleTypeListResponse{
    return ArticleTypeListResponse(
        contents = this.content.map { it.toResponse() },
        totalElements = this.totalElements,
        totalPages = this.totalPages,
    )
}