package com.polyhabr.poly_back.entity

import javax.persistence.*

@Entity
@Table(name = "tag_type")
class TagType (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    var name: String
)