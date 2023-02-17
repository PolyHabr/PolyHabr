package com.polyhabr.poly_back.controller.model.userToLikedArticle.request

import com.polyhabr.poly_back.dto.UserToLikedArticleDto
import com.polyhabr.poly_back.entity.Article
import com.polyhabr.poly_back.entity.User
import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable
import javax.validation.constraints.NotNull

@Schema(
    name = "UserToLikedArticleRequest",
    description = "Data object for User To Liked Article Request"
)

data class UserToLikedArticleRequest(
    @field:NotNull
    val articleId: Article? = null,

    @field:NotNull
    val userId: User? = null,
): Serializable

fun UserToLikedArticleRequest.toDto() = UserToLikedArticleDto(
    articleId = this.articleId!!,
    userId = this.userId!!,
)