package com.polyhabr.poly_back.entity

import com.polyhabr.poly_back.controller.model.articleType.request.ArticleTypeRequest
import com.polyhabr.poly_back.dto.ArticleTypeDto
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "article_type")
open class ArticleType() : BaseEntity<Long>() {
    @Size(max = 255)
    @Column(name = "name")
    open var name: String? = null

    constructor(name: String) : this() {
        this.name = name
    }
}

fun ArticleType.toDto() = ArticleTypeDto(
    id = this.id,
    name = this.name,
)
