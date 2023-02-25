package com.polyhabr.poly_back.controller.model.article.response

import com.polyhabr.poly_back.dto.ArticleDto
import com.polyhabr.poly_back.entity.ArticleType
import com.polyhabr.poly_back.entity.auth.User
import java.time.LocalDate

data class ArticleResponse(
    val date: LocalDate,
    val filePdf: String? = null,
    val likes: Int,
    val previewText: String,
    val typeId: ArticleType,
    val userId: User,
)

fun ArticleDto.toResponse(): ArticleResponse {
    return ArticleResponse(
        date = this.date!!,
        filePdf = this.filePdf!!,
        likes = this.likes!!,
        previewText = this.previewText!!,
        typeId = this.typeId!!,
        userId = this.userId!!,
    )
}