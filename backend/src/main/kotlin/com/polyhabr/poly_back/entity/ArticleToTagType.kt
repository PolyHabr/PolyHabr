package com.polyhabr.poly_back.entity

import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "article_to_tag_type")
class ArticleToTagType (
    var article_id: Int,
    var tag_type_id: Int
)