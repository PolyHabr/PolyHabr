package com.polyhabr.poly_back.dto

import com.polyhabr.poly_back.entity.ArticleType
import com.polyhabr.poly_back.entity.auth.User
import java.time.LocalDate

data class ArticleDto(
    val id: Long? = null,
    var date: LocalDate,
    var filePdf: String? = null,
    var likes: Int,
    var previewText: String,
    var title: String,
    var typeId: ArticleType? = null,
    val typeName: String? = null,
    var userId: User? = null,
    val listDisciplineName: List<String>,
    val listTag: List<String>,
)
