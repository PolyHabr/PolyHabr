package com.polyhabr.poly_back.controller.model.comment.request

import com.polyhabr.poly_back.dto.CommentDto
import com.polyhabr.poly_back.entity.Article
import com.polyhabr.poly_back.entity.User
import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable
import javax.validation.constraints.NotNull

@Schema(
    name = "CommentRequest",
    description = "Data object for Comment Request",
)
data class CommentRequest(
    @field:Schema(example = "Hi! You have a mistake here...")
    @field:NotNull
    var text: String? = null,

    @field:Schema(example = "article id")
    @field:NotNull
    var articleId: Article? = null,

    @field:Schema(example = "user id")
    @field:NotNull
    var userId: User? = null,
): Serializable

fun CommentRequest.toDto() = CommentDto(
    text = this.text!!,
    articleId = this.articleId!!,
    userId = this.userId!!,
)