package com.polyhabr.poly_back.controller.model.userToLikedArticle.response

import com.polyhabr.poly_back.dto.UserToLikedArticleDto
import com.polyhabr.poly_back.entity.Article
import com.polyhabr.poly_back.entity.User

data class UserToLikedArticleResponse(
    val articleId: Article,
    val userId: User,
)

fun UserToLikedArticleDto.toResponse(): UserToLikedArticleResponse {
    return UserToLikedArticleResponse(
        articleId = this.articleId!!,
        userId = this.userId!!,
    )
}