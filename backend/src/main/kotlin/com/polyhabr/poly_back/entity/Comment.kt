package com.polyhabr.poly_back.entity

import javax.persistence.*

@Entity
@Table(name = "comment")
class Comment (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    var text: String,

    @ManyToOne
    @JoinColumn(name = "article_id")
    var article_to_comment: Article,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var users_to_comment: Users,

)
