package com.polyhabr.poly_back.dto

import com.polyhabr.poly_back.entity.Article
import com.polyhabr.poly_back.entity.DisciplineType

data class ArticleToDisciplineTypeDto(
    val id: Long? = null,
    var articleId: Article? = null,
    var disciplineTypeId: DisciplineType? = null,
)
