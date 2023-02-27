package com.polyhabr.poly_back.dto

data class ArticleUpdateDto(
    var filePdf: String? = null,
    var likes: Int? = null,
    var previewText: String? = null,
    var title: String? = null,
    var text: String? = null,
    var typeName: String? = null,
)
