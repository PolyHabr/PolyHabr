package com.polyhabr.poly_back.controller.model.article.response

import com.polyhabr.poly_back.dto.ArticleDto
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.Page

data class ArticleListResponse(
    @field:Schema(description = "List of articles")
    val contents: List<ArticleResponse>,
    @field:Schema(description = "Total number of elements")
    val totalElements: Long,
    @field:Schema(description = "Total number of pages")
    val totalPages: Int,
)

fun Page<ArticleDto>.toListResponse(): ArticleListResponse{
    return ArticleListResponse(
        contents = this.content.map{ it.toResponse()},
        totalElements = this.totalElements,
        totalPages = this.totalPages,
    )
}