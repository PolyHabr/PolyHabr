package com.polyhabr.poly_back.entity

import java.sql.Date
import javax.persistence.*

@Entity
@Table(name = "article")
class Article(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    var user_id: Int,
    var type_id: Int,
    var preview_text: String,
    var date: Date,
    var file_pdf: String,
    var likes: Int = 0,

    @ManyToMany
    @JoinTable(
        name = "article_to_tag_type",
        joinColumns = arrayOf(JoinColumn(name = "article_id")),
        inverseJoinColumns = arrayOf(JoinColumn(name = "tag_type_id"))
    )
    var to_tag: List<TagType>,

    @ManyToMany
    @JoinTable(
    name = "article_to_discipline_type",
    joinColumns = arrayOf(JoinColumn(name = "article_id")),
    inverseJoinColumns = arrayOf(JoinColumn(name = "discipline_type_id"))
    )
    var to_discipline: List<DisciplineType>,

    @ManyToMany
    @JoinTable(
        name = "user_to_liked_article",
        joinColumns = arrayOf(JoinColumn(name = "user_id")),
        inverseJoinColumns = arrayOf(JoinColumn(name = "article_id"))
    )
    var to_liked_article: List<User>,

    @OneToMany(mappedBy = "comments")
    var articles: List<Comment> = emptyList(),

    @OneToMany(mappedBy = "article_types")
    var types: List<ArticleType> = emptyList(),

    @OneToMany(mappedBy = "users_id")
    var users: List<User> = emptyList(),

    )
