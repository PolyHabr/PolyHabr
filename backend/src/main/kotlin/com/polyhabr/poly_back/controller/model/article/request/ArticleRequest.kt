package com.polyhabr.poly_back.controller.model.article.request

import com.polyhabr.poly_back.dto.ArticleDto
import io.swagger.v3.oas.annotations.media.Schema
import org.joda.time.DateTime
import java.io.Serializable
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.PositiveOrZero

@Schema(
    name = "ArticleRequest",
    description = "Data object for Article Request",
)

data class ArticleRequest(

    @field:Schema(example = "title, not null")
    @field:NotNull
    val title: String? = null,

    @field:Schema(example = "text, not null")
    @field:NotNull
    val text: String? = null,

    @field:Schema(example = "some text, not null")
    @field:NotNull
    val previewText: String? = null,

    @field:Schema(example = "base 64, nullable")
    val filePdf: String? = null,

    @field:Schema(example = "type name, not null")
    @field:NotNull
    val articleType: String? = null,

    @field:Schema(example = "list discipline name, not null and not empty, max size 5")
    @field:NotNull
    @field:NotEmpty
    val listDisciplineName: List<String>? = null,

    @field:Schema(example = "list tag, not null and not empty, max size 5")
    @field:NotNull
    @field:NotEmpty
    val listTag: List<String>? = null,
) : Serializable

fun ArticleRequest.toDtoWithoutType(): ArticleDto {
    if (this.listDisciplineName!!.size > 5) {
        throw IllegalArgumentException("listDisciplineName size must be less than 5")
    }
    if (this.listTag!!.size > 5) {
        throw IllegalArgumentException("listTag size must be less than 5")
    }
    return ArticleDto(
        date = DateTime.now(),
        likes = 0,
        previewText = this.previewText!!,
        title = this.title!!,
        listDisciplineName = this.listDisciplineName,
        listTag = this.listTag,
        typeName = this.articleType!!,
        text = this.text!!,
        isSaveToFavourite = false,
        pdfId = null,
        previewImgId = null
    )
}

fun ArticleRequest.toDtoWithoutType(data: ByteArray?, originName: String?): ArticleDto {
    if (this.listDisciplineName!!.size > 5) {
        throw IllegalArgumentException("listDisciplineName size must be less than 5")
    }
    if (this.listTag!!.size > 5) {
        throw IllegalArgumentException("listTag size must be less than 5")
    }
    return ArticleDto(
        date = DateTime.now(),
        likes = 0,
        previewText = this.previewText!!,
        title = this.title!!,
        listDisciplineName = this.listDisciplineName,
        listTag = this.listTag,
        typeName = this.articleType!!,
        text = this.text!!,
        isSaveToFavourite = false,
        pdfId = null,
        previewImgId = null
    )
}