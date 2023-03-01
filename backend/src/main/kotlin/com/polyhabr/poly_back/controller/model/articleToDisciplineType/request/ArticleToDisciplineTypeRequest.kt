package com.polyhabr.poly_back.controller.model.articleToDisciplineType.request

import com.polyhabr.poly_back.dto.ArticleToDisciplineTypeDto
import com.polyhabr.poly_back.entity.Article
import com.polyhabr.poly_back.entity.DisciplineType
import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable
import javax.validation.constraints.NotNull

@Schema(
    name = "ArticleToDisciplineTypeRequest",
    description = "Data object for Article To Discipline Type Request"
)

data class ArticleToDisciplineTypeRequest(
    @field:NotNull
    val articleId: Article? = null,

    @field:NotNull
    val disciplineTypeId: DisciplineType? = null,
):Serializable

fun ArticleToDisciplineTypeRequest.toDto() = ArticleToDisciplineTypeDto(
    articleId = this.articleId!!,
    disciplineTypeId = this.disciplineTypeId!!,
)