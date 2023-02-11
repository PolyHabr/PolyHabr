package com.polyhabr.poly_back.dto

import com.polyhabr.poly_back.entity.ArticleType
import com.polyhabr.poly_back.entity.User
import java.time.LocalDate

data class ArticleDto(
    val id: Long? = null,
    var date: LocalDate? = null,
    var filePdf: String? = null,
    var likes: Int? = null,
    var previewText: String? = null,
    var typeId: ArticleType? = null,
    var userId: User? = null,
)
