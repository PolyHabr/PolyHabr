package com.polyhabr.poly_back.dto

import com.polyhabr.poly_back.entity.Article
import com.polyhabr.poly_back.entity.auth.User

data class UserToLikedArticleDto (
    val id: Long? = null,
    var articleId: Article? = null,
    var userId: User? = null,
)