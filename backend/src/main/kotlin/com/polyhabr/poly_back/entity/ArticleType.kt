package com.polyhabr.poly_back.entity

import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "article_type")
open class ArticleType : BaseEntity<Long>() {
    @Size(max = 255)
    @Column(name = "name")
    open var name: String? = null
}
