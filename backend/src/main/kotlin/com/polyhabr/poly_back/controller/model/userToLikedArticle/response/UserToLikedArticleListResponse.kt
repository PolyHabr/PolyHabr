package com.polyhabr.poly_back.controller.model.userToLikedArticle.response

import com.polyhabr.poly_back.dto.UserToLikedArticleDto

data class UserToLikedArticleListResponse(
    val userToLikedArticles: List<UserToLikedArticleResponse>,
    val page: Int,
)

fun List<UserToLikedArticleDto>.toResponse(): UserToLikedArticleListResponse {
    return UserToLikedArticleListResponse(
        userToLikedArticles = this.map { it.toResponse() },
        page = 0 // todo
    )
}