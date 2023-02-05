package com.polyhabr.poly_back.dto

data class CommentDto(
    val id: Long? = null,
    var text: String? = null,
    var articleId: Long? = null,
    var userId: Long? = null,
)
