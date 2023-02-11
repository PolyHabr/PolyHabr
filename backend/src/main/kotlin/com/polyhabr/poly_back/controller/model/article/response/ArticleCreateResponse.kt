package com.polyhabr.poly_back.controller.model.article.response

data class ArticleCreateResponse(
    val isSuccess: Boolean,
    val id: Long? = null,
)