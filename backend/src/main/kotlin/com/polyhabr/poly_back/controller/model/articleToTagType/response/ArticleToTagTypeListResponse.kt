package com.polyhabr.poly_back.controller.model.articleToTagType.response

import com.polyhabr.poly_back.dto.ArticleToTagTypeDto

data class ArticleToTagTypeListResponse(
    val articleToTagTypes: List<ArticleToTagTypeResponse>,
    val page: Int,
)

fun List<ArticleToTagTypeDto>.toResponse(): ArticleToTagTypeListResponse {
    return ArticleToTagTypeListResponse(
        articleToTagTypes = this.map { it.toResponse() },
        page = 0, // todo
    )
}