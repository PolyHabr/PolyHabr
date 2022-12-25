package com.polyhabr.poly_back.entity

import org.springframework.context.annotation.Primary
import javax.persistence.*

@Entity
@Table(name = "article_type")
class ArticleType (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    var name: String,

    @ManyToOne
    @JoinColumn(name = "id")
    var article_types: Article,
)