package com.polyhabr.poly_back.controller.model.articleType.response

import com.polyhabr.poly_back.dto.ArticleTypeDto

data class ArticleTypeResponse(
    var name: String,
)

fun ArticleTypeDto.toResponse(): ArticleTypeResponse{
    return ArticleTypeResponse(
        name = this.name!!,
    )
}