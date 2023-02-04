package com.polyhabr.poly_back.dto

import com.polyhabr.poly_back.entity.ArticleType
import com.polyhabr.poly_back.entity.Users
import java.sql.Date

data class ArticleDto(
    val id: Int? = null,
    var date: Date,
    var file_pdf: String,
    var likes: Int = 0,
    var preview_text: String,
    var type_id: ArticleType,
    var user_id: Users,
    )
