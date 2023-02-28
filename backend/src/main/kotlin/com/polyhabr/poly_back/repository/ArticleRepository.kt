package com.polyhabr.poly_back.repository

import com.polyhabr.poly_back.entity.Article
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ArticleRepository: JpaRepository<Article, Long>{
    @Query("SELECT a FROM Article a WHERE a.previewText LIKE %:pname%")
    fun findArticleByName(pageable: Pageable, @Param("pname") pname: String): Page<Article>

    @Query("SELECT a FROM Article a WHERE a.title LIKE %:pname%")
    fun findArticleByTitle(pageable: Pageable, @Param("pname") pname: String): Page<Article>

    @Query("SELECT a FROM Article a WHERE a.userId.id = :id")
    fun findArticleByUserId(pageable: Pageable, @Param("id") id: Long): Page<Article>

    @Modifying
    @Query("UPDATE Article a SET a.likes = a.likes + 1 WHERE a.id = :id")
    fun addLikeByArticleId(@Param("id") id: Long)

    @Modifying
    @Query("UPDATE Article a SET a.likes = a.likes - 1 WHERE a.id = :id")
    fun decreaseLikeByArticleId(@Param("id") id: Long)

}
