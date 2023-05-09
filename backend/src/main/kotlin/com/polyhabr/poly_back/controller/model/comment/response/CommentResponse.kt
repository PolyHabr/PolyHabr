package com.polyhabr.poly_back.controller.model.comment.response

import com.polyhabr.poly_back.controller.model.article.response.toResponse
import com.polyhabr.poly_back.controller.model.user.response.UserOtherResponse
import com.polyhabr.poly_back.controller.model.user.response.toOtherResponse
import com.polyhabr.poly_back.dto.CommentDto
import com.polyhabr.poly_back.entity.Article
import com.polyhabr.poly_back.entity.auth.User
import com.polyhabr.poly_back.entity.auth.toDtoWithoutPasswordAndEmail
import com.polyhabr.poly_back.utility.DateTimeUtils

data class CommentResponse(
    val id: Long,
    val text: String,
    val articleId: Long,
    val userId: UserOtherResponse,
    val data: String
)

fun CommentDto.toResponse(): CommentResponse {
    return CommentResponse(
        id = this.id!!,
        text = this.text!!,
        articleId = this.articleId!!.id!!,
        userId = this.userId!!.toDtoWithoutPasswordAndEmail().toOtherResponse(),
        data = this.data!!.toString(DateTimeUtils.defaultFormat)
    )
}