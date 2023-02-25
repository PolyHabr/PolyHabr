package com.polyhabr.poly_back.repository

import com.polyhabr.poly_back.entity.ArticleType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ArticleTypeRepository: JpaRepository<ArticleType, Long>{
    @Query("SELECT at FROM ArticleType at WHERE at.name LIKE %:pname%")
    fun findArticleTypesByName(pageable: Pageable, @Param("pname") pname: String): Page<ArticleType>

    fun findByName(@Param("name") name: String): ArticleType?
}
