package com.polyhabr.poly_back.controller.model.articleToTagType.response

import com.polyhabr.poly_back.dto.ArticleToTagTypeDto
import com.polyhabr.poly_back.entity.Article
import com.polyhabr.poly_back.entity.TagType

data class ArticleToTagTypeResponse(
    val articleId: Article,
    val tagTypeId: TagType,
)

fun ArticleToTagTypeDto.toResponse(): ArticleToTagTypeResponse{
    return ArticleToTagTypeResponse(
        articleId = this.articleId!!,
        tagTypeId = this.tagTypeId!!,
    )
}