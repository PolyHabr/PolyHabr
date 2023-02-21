package com.polyhabr.poly_back.controller.model.articleType.request

import com.polyhabr.poly_back.dto.ArticleTypeDto
import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotNull

@Schema(
    name = "ArticleTypeRequest",
    description = "Data object for Article Type Request",
)

data class ArticleTypeRequest(
    @field:Schema(example = "abstract")
    @field:NotNull
    var name: String? = null,
)

fun ArticleTypeRequest.toDto() = ArticleTypeDto(
    name = this.name!!,
)