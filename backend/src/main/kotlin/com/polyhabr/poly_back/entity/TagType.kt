//package com.polyhabr.poly_back.entity
//
//import javax.persistence.*
//
//@Entity
//@Table(name = "tag_type")
//class TagType (
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    val id: Int,
//    var name: String,
//    @ManyToMany(mappedBy = "to_tag")
//    var article_to_tag_type: List<Article>,
//)