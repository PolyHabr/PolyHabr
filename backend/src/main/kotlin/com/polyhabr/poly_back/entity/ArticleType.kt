package com.polyhabr.poly_back.entity

import javax.persistence.*

@Entity
@Table(name = "article_type")
class ArticleType (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    var name: String
)