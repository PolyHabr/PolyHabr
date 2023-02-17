package com.polyhabr.poly_back.controller.model.articleToDisciplineType.response

import com.polyhabr.poly_back.dto.ArticleToDisciplineTypeDto

data class ArticleToDisciplineTypeListResponse(
    val articleToDisciplineTypes: List<ArticleToDisciplineTypeResponse>,
    val page: Int,
)

fun List<ArticleToDisciplineTypeDto>.toResponse(): ArticleToDisciplineTypeListResponse {
    return ArticleToDisciplineTypeListResponse(
        articleToDisciplineTypes = this.map { it.toResponse() },
        page = 0, // todo
    )
}