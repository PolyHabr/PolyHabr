package com.polyhabr.poly_back.entity

import javax.persistence.*

@Entity
@Table(name = "discipline_type")
class DisciplineType(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    var name: String,

    @ManyToMany(mappedBy = "to_discipline")
    var article_to_discipline_type: List<Article>,

    )
