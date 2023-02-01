package com.polyhabr.poly_back.entity

import javax.persistence.*

@Entity
@Table(name = "users")
class Users (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    var email: String,
    var login: String,
    var password: String,
    var name_u: String,
    var surname: String ?= null,

    @OneToMany (mappedBy = "users")
    var user_id: List<Article>,

    @ManyToMany(mappedBy = "to_liked")
    var user_to_liked_article: List<Article>,

    @OneToMany (mappedBy = "users_to_comment")
    var users_comment: List<Comment>,

){

}
