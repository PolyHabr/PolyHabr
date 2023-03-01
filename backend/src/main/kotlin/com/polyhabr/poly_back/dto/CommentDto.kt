package com.polyhabr.poly_back.dto

import com.polyhabr.poly_back.entity.Article
import com.polyhabr.poly_back.entity.auth.User
import org.joda.time.DateTime

data class CommentDto(
    val id: Long? = null,
    var text: String? = null,
    var articleId: Article? = null,
    var userId: User? = null,
    var data: DateTime? = null
)
