package com.polyhabr.poly_back.controller.model.comment.response

import com.polyhabr.poly_back.dto.CommentDto

data class CommentListResponse (
    val comments: List<CommentResponse>,
    val page: Int,
)

fun List<CommentDto>.toResponse(): CommentListResponse{
    return CommentListResponse(
        comments = this.map{it.toResponse()},
        page = 0, //todo
    )
}