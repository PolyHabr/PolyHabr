package com.polyhabr.poly_back.repository

import com.polyhabr.poly_back.entity.Comment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CommentRepository : JpaRepository<Comment, Long> {
    @Query("SELECT u FROM Comment u WHERE LOWER(u.text) LIKE %:pname% ORDER BY u.date DESC")
    fun findCommentsByName(pageable: Pageable, @Param("pname") pname: String): Page<Comment>

    @Query("SELECT u FROM Comment u WHERE u.articleId.id = :id ORDER BY u.date DESC")
    fun findAllByArticleId(pageable: Pageable, @Param("id") id: Long): Page<Comment>
}
