package com.polyhabr.poly_back.controller.model.article.request

import com.polyhabr.poly_back.dto.ArticleUpdateDto
import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable

@Schema(
    name = "ArticleUpdateRequest",
    description = "Data object for Article Update Request, all field nullable",
)
data class ArticleUpdateRequest(
    val title: String? = null,
    val previewText: String? = null,
    val filePdf: String? = null,
    val likes: Int? = 0,
    val typeName: String? = null,
) : Serializable

fun ArticleUpdateRequest.toDto(): ArticleUpdateDto {
    return ArticleUpdateDto(
        filePdf = this.filePdf,
        likes = this.likes,
        previewText = this.previewText,
        title = this.title,
        typeName = this.typeName,
    )
}

fun ArticleUpdateRequest.toDtoOnlyLike(): ArticleUpdateDto{
    return ArticleUpdateDto(
        likes = this.likes
    )
}