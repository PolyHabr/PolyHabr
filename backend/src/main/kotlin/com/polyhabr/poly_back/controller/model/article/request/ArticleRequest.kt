package com.polyhabr.poly_back.controller.model.article.request

import com.polyhabr.poly_back.dto.ArticleDto
import com.polyhabr.poly_back.entity.DisciplineType
import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable
import java.time.LocalDate
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.PositiveOrZero
import javax.validation.constraints.Size

@Schema(
    name = "ArticleRequest",
    description = "Data object for Article Request",
)

data class ArticleRequest(

    @field:Schema(example = "title, not null")
    @field:NotNull
    val title: String? = null,

    @field:Schema(example = "some text, not null")
    @field:NotNull
    val previewText: String? = null,

    @field:Schema(example = "base 64, nullable")
    val filePdf: String? = null,

    @field:Schema(example = "positive number, nullable")
    @field:PositiveOrZero
    val likes: Int? = 0,

    @field:Schema(example = "type name, not null")
    @field:NotNull
    val typeName: String? = null,

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
        date = LocalDate.now(),
        filePdf = this.filePdf,
        likes = this.likes ?: 0,
        previewText = this.previewText!!,
        title = this.title!!,
        listDisciplineName = this.listDisciplineName,
        listTag = this.listTag,
        typeName = this.typeName!!,
    )
}