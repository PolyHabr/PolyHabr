package com.polyhabr.poly_back.repository

import com.polyhabr.poly_back.entity.Article
import com.polyhabr.poly_back.entity.ArticleToTagType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface ArticleToTagTypeRepository : JpaRepository<ArticleToTagType, Long> {
    fun findByArticle(@Param("article") article: Article): List<ArticleToTagType>
}