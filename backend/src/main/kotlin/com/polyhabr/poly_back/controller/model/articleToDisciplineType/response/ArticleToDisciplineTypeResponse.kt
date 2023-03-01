package com.polyhabr.poly_back.controller.model.articleToDisciplineType.response

import com.polyhabr.poly_back.dto.ArticleToDisciplineTypeDto
import com.polyhabr.poly_back.entity.Article
import com.polyhabr.poly_back.entity.DisciplineType

data class ArticleToDisciplineTypeResponse(
    val articleId: Article,
    val disciplineTypeId: DisciplineType,
)

fun ArticleToDisciplineTypeDto.toResponse(): ArticleToDisciplineTypeResponse{
    return ArticleToDisciplineTypeResponse(
        articleId = this.articleId!!,
        disciplineTypeId = this.disciplineTypeId!!,
    )
}