package com.polyhabr.poly_back.repository

import com.polyhabr.poly_back.entity.Article
import com.polyhabr.poly_back.entity.ArticleToFav
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ArticleToFavRepository : JpaRepository<ArticleToFav, Long> {
    @Query("SELECT a FROM ArticleToFav a WHERE a.userId.id = :id ORDER BY a.id DESC")
    fun findByUserId(pageable: Pageable, @Param("id") id: Long): Page<ArticleToFav>

    @Query("SELECT a FROM ArticleToFav a WHERE a.articleId.id = :id ORDER BY a.id DESC")
    fun findByArticleId(pageable: Pageable, @Param("id") id: Long): Page<ArticleToFav>

    @Query("SELECT a FROM ArticleToFav a WHERE a.articleId.id = :id ORDER BY a.id DESC")
    fun byArticleId(@Param("id") id: Long): ArticleToFav?
}