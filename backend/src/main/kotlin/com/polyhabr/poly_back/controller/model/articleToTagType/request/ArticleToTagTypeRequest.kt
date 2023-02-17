package com.polyhabr.poly_back.controller.model.articleToTagType.request

import com.polyhabr.poly_back.dto.ArticleToTagTypeDto
import com.polyhabr.poly_back.entity.Article
import com.polyhabr.poly_back.entity.TagType
import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable
import javax.validation.constraints.NotNull

@Schema(
    name = "ArticleToTagTypeRequest",
    description = "Data object for Article To Tag Type Request"
)

data class ArticleToTagTypeRequest(
    @field:NotNull
    val articleId: Article? = null,

    @field:NotNull
    val tagTypeId: TagType? = null,
): Serializable

fun ArticleToTagTypeRequest.toDto() = ArticleToTagTypeDto(
    articleId = this.articleId!!,
    tagTypeId = this.tagTypeId!!,
)