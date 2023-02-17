package com.polyhabr.poly_back.controller.model.comment.response

import com.polyhabr.poly_back.dto.CommentDto
import com.polyhabr.poly_back.entity.Article
import com.polyhabr.poly_back.entity.User

data class CommentResponse (
    val text: String,
    val articleId: Long,
    val userId: Long,
)

fun CommentDto.toResponse(): CommentResponse{
    return CommentResponse(
        text = this.text!!,
        articleId = this.articleId!!,
        userId = this.userId!!,
    )
}