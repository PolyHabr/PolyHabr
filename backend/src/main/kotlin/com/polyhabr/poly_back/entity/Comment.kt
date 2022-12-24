package com.polyhabr.poly_back.entity

import javax.persistence.*

@Entity
@Table(name = "comment")
class Comment (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    var text: String,
    var user_id: Int,
    var article_id: Int
)