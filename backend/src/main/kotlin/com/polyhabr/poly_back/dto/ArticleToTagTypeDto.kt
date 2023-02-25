package com.polyhabr.poly_back.dto

import com.polyhabr.poly_back.entity.Article
import com.polyhabr.poly_back.entity.TagType

data class ArticleToTagTypeDto(
    val id: Long? = null,
    var articleId: Article? = null,
    var tagTypeId: TagType? = null,
)
