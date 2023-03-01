package com.polyhabr.poly_back.controller.model.comment.response

import com.polyhabr.poly_back.controller.model.article.response.toResponse
import com.polyhabr.poly_back.dto.CommentDto
import com.polyhabr.poly_back.entity.Article
import com.polyhabr.poly_back.entity.auth.User
import com.polyhabr.poly_back.utility.DateTimeUtils

data class CommentResponse (
    val text: String,
    val articleId: Article,
    val userId: User,
    val data: String
)

fun CommentDto.toResponse(): CommentResponse{
    return CommentResponse(
        text = this.text!!,
        articleId = this.articleId!!,
        userId = this.userId!!,
        data = this.data!!.toString(DateTimeUtils.defaultFormat)
    )
}