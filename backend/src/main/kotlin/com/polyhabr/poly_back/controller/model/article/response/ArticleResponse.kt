package com.polyhabr.poly_back.controller.model.article.response

import com.polyhabr.poly_back.controller.model.user.response.UserOtherResponse
import com.polyhabr.poly_back.controller.model.user.response.toOtherResponse
import com.polyhabr.poly_back.dto.ArticleDto
import com.polyhabr.poly_back.entity.ArticleType
import com.polyhabr.poly_back.entity.auth.toDto
import com.polyhabr.poly_back.utility.DateTimeUtils
import org.joda.time.DateTime
import java.time.LocalDate
import javax.persistence.Id

data class ArticleResponse(
    val id: Long,
    val date: String,
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
    val viewCount: Long
)

fun ArticleDto.toResponse(): ArticleResponse {
    return ArticleResponse(
        id = this.id!!,
        date = this.date.toString(DateTimeUtils.defaultFormat),
        filePdf = this.filePdf,
        likes = this.likes,
        previewText = this.previewText,
        typeId = this.typeId!!,
        user = this.userId?.toDto()?.toOtherResponse()!!,
        title = this.title,
        listDisciplineName = this.listDisciplineName,
        listTag = this.listTag,
        text = this.text,
        fileId = this.fileId,
        viewCount = this.viewCount
    )
}