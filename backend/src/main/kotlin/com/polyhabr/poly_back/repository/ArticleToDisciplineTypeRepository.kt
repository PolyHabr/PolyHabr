package com.polyhabr.poly_back.repository

import com.polyhabr.poly_back.entity.Article
import com.polyhabr.poly_back.entity.ArticleToDisciplineType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface ArticleToDisciplineTypeRepository : JpaRepository<ArticleToDisciplineType, Long> {
    fun findByArticle(@Param("article") article: Article): List<ArticleToDisciplineType>
}