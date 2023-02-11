package com.polyhabr.poly_back.controller.model.article.response

import com.polyhabr.poly_back.dto.ArticleDto
data class ArticleListResponse(
    val articles: List<ArticleResponse>,
    val page: Int,
)

fun List<ArticleDto>.toResponse(): ArticleListResponse{
    return ArticleListResponse(
        articles = this.map{ it.toResponse()},
        page = 0, // todo
    )
}