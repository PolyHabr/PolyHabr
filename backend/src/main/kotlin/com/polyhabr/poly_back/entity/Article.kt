package com.polyhabr.poly_back.entity

import java.sql.Date
import javax.persistence.*


@Entity
@Table(name = "article")
class Article(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var users: Users,

    @ManyToMany
    @JoinTable(
    name = "article_to_tag_type",
    joinColumns = arrayOf(JoinColumn(name = "article_id")),
    inverseJoinColumns = arrayOf(JoinColumn(name = "tag_type_id"))
    )
    var to_tag_type: List<TagType>,

    @ManyToMany
    @JoinTable(
    name = "article_to_discipline_type",
    joinColumns = arrayOf(JoinColumn(name = "article_id")),
    inverseJoinColumns = arrayOf(JoinColumn(name = "discipline_type_id"))
    )
    var to_discipline: List<DisciplineType>,

    @ManyToOne
    @JoinColumn(name = "type_id")
    var articlesType: ArticleType,

    @ManyToMany
    @JoinTable(
        name = "user_to_liked_article",
        joinColumns = arrayOf(JoinColumn(name = "article_id")),
        inverseJoinColumns = arrayOf(JoinColumn(name = "user_id"))
    )
    var to_liked: List<Users>,

    @OneToMany (mappedBy = "article_to_comment")
    var article_comment: List<Comment>,

    var preview_text: String,
    var date: Date,
    var file_pdf: String,
    var likes: Int = 0,

    )
