package com.polyhabr.poly_back.dto

import com.polyhabr.poly_back.entity.ArticleType
import com.polyhabr.poly_back.entity.auth.User
import org.joda.time.DateTime
import java.time.LocalDate

data class ArticleDto(
    val id: Long? = null,
    var date: DateTime,
    var filePdf: String? = null,
    var likes: Int,
    var previewText: String,
    var title: String,
    val text: String,
    var typeId: ArticleType? = null,
    val typeName: String? = null,
    var userId: User? = null,
    val listDisciplineName: List<String>,
    val listTag: List<String>,
    val fileDto: FileCreatingDto? = null,
    var fileId: String? = null,
    var viewCount: Long = 0,
    val isSaveToFavourite: Boolean
)
