package com.polyhabr.poly_back.repository

import com.polyhabr.poly_back.entity.Article
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ArticleRepository: JpaRepository<Article, Long>{
    @Query("SELECT a FROM Article a WHERE a.previewText LIKE %:pname%")
    fun findArticleByName(pageable: Pageable, @Param("pname") pname: String): Page<Article>
}
