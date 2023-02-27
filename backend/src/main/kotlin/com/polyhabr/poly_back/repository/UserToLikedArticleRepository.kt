package com.polyhabr.poly_back.repository

import com.polyhabr.poly_back.entity.UserToLikedArticle
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UserToLikedArticleRepository : JpaRepository<UserToLikedArticle, Long> {
    @Query("select l.id from UserToLikedArticle l where l.user.id = :user_id and l.article.id = :article_id")
    fun findArticleWithLike(@Param("article_id") article_id: Long, @Param("user_id") user_id: Long?): Long?
}