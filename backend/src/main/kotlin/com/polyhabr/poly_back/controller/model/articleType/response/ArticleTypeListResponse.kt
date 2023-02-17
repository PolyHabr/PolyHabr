package com.polyhabr.poly_back.controller.model.articleType.response

import com.polyhabr.poly_back.dto.ArticleTypeDto

data class ArticleTypeListResponse(
    val articleTypes: List<ArticleTypeResponse>,
    val page: Int,
)

fun List<ArticleTypeDto>.toResponse(): ArticleTypeListResponse{
    return ArticleTypeListResponse(
        articleTypes = this.map {it.toResponse()},
        page = 0, // todo
    )
}