package com.polyhabr.poly_back.controller.model.article.response

import com.polyhabr.poly_back.controller.model.user.response.UserOtherResponse
import com.polyhabr.poly_back.controller.model.user.response.toOtherResponse
import com.polyhabr.poly_back.dto.ArticleDto
import com.polyhabr.poly_back.entity.ArticleType
import com.polyhabr.poly_back.entity.auth.toDto
import java.time.LocalDate

data class ArticleResponse(
    val date: LocalDate,
    val filePdf: String? = null,
    val likes: Int,
    val previewText: String,
    val typeId: ArticleType,
    val user: UserOtherResponse? = null,
    val title: String? = null,
    val text: String? = null,
    val listDisciplineName: List<String>,
    val listTag: List<String>,
    val fileId: String? = null,
)

fun ArticleDto.toResponse(): ArticleResponse {
    return ArticleResponse(
        date = this.date,
        filePdf = this.filePdf,
        likes = this.likes,
        previewText = this.previewText,
        typeId = this.typeId!!,
        user = this.userId?.toDto()?.toOtherResponse()!!,
        title = this.title,
        listDisciplineName = this.listDisciplineName,
        listTag = this.listTag,
        text = this.text,
        fileId = this.fileId
    )
}