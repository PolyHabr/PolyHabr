package com.polyhabr.poly_back.controller.model.article.request

import com.polyhabr.poly_back.dto.ArticleDto
import com.polyhabr.poly_back.entity.ArticleType
import com.polyhabr.poly_back.entity.auth.User
import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable
import java.time.LocalDate
import javax.validation.constraints.NotNull
import javax.validation.constraints.PositiveOrZero

@Schema(
    name = "ArticleRequest",
    description = "Data object for Article Request",
)

data class ArticleRequest(

    @field:Schema(example = "date")
    @field:NotNull
    val date: LocalDate? = null,

    @field:Schema(example = "base 64")
    @field:NotNull
    val filePdf: String? = null,

    @field:Schema(example = "positive number")
    @field:NotNull
    @field:PositiveOrZero
    val likes: Int? = null,

    @field:Schema(example = "some text")
    @field:NotNull
    val previewText: String? = null,

    @field:Schema(example = "type id")
    @field:NotNull
    val typeId: ArticleType? = null,

    @field:Schema(example = "user id")
    @field:NotNull
    val userId: User? = null,
): Serializable

fun ArticleRequest.toDto() = ArticleDto(
    date = this.date!!,
    filePdf = this.filePdf!!,
    likes = this.likes!!,
    previewText = this.previewText!!,
    typeId = this.typeId!!,
    userId = this.userId!!,
)