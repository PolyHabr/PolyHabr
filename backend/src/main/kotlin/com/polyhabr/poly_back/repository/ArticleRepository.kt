package com.polyhabr.poly_back.repository

import com.polyhabr.poly_back.entity.Article
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ArticleRepository : JpaRepository<Article, Long> {

    @Query("SELECT a FROM Article a WHERE LOWER(a.previewText) LIKE %:pname% ORDER BY a.date DESC ")
    fun findManual(pageable: Pageable, @Param("pname") pname: String): Page<Article>

    @Query("SELECT a FROM Article a WHERE LOWER(a.previewText) LIKE %:pname% ORDER BY a.date DESC ")
    fun findArticleByName(pageable: Pageable, @Param("pname") pname: String): Page<Article>

    @Query("SELECT a FROM Article a WHERE LOWER(a.title) LIKE %:pname% ORDER BY a.date DESC")
    fun findArticleByTitle(pageable: Pageable, @Param("pname") pname: String): Page<Article>

    @Query("SELECT a FROM Article a WHERE a.userId.id = :id ORDER BY a.date DESC")
    fun findArticleByUserId(pageable: Pageable, @Param("id") id: Long): Page<Article>

    @Modifying
    @Query("UPDATE Article a SET a.likes = a.likes + 1 WHERE a.id = :id")
    fun addLikeByArticleId(@Param("id") id: Long)

    @Modifying
    @Query("UPDATE Article a SET a.likes = a.likes - 1 WHERE a.id = :id")
    fun decreaseLikeByArticleId(@Param("id") id: Long)

    @Query("SELECT a FROM Article a ORDER BY a.date DESC ")
    fun findArticlesOrderDate(pageable: Pageable): Page<Article>

    @Query("SELECT a FROM Article a where :nowmillis - a.date <= :diffmillis ORDER BY a.view DESC, a.date DESC")
    fun findArticlesWithLimitTimelineOrderView(
        pageable: Pageable,
        @Param("nowmillis") nowmillis: Long,
        @Param("diffmillis") diffmillis: Long,
    ): Page<Article>

    @Query("SELECT a FROM Article a where :nowmillis - a.date <= :diffmillis ORDER BY a.view ASC, a.date DESC")
    fun findArticlesWithLimitTimelineOrderViewASC(
        pageable: Pageable,
        @Param("nowmillis") nowmillis: Long,
        @Param("diffmillis") diffmillis: Long,
    ): Page<Article>

    @Query("SELECT a FROM Article a where :nowmillis - a.date <= :diffmillis ORDER BY a.likes DESC, a.date DESC")
    fun findArticlesWithLimitTimelineOrderLike(
        pageable: Pageable,
        @Param("nowmillis") nowmillis: Long,
        @Param("diffmillis") diffmillis: Long,
    ): Page<Article>

    @Query("SELECT a FROM Article a where :nowmillis - a.date <= :diffmillis ORDER BY a.likes ASC, a.date DESC")
    fun findArticlesWithLimitTimelineOrderLikeASC(
        pageable: Pageable,
        @Param("nowmillis") nowmillis: Long,
        @Param("diffmillis") diffmillis: Long,
    ): Page<Article>

    @Query("SELECT a FROM Article a where LOWER(a.title) LIKE %:titlesearch% ORDER BY a.date DESC, a.date DESC")
    fun searchByTitleArticlesOrderDate(pageable: Pageable,  @Param("titlesearch") titlesearch: String): Page<Article>

    @Query("SELECT a FROM Article a where :nowmillis - a.date <= :diffmillis and LOWER(a.title) LIKE %:titlesearch% ORDER BY a.view DESC, a.date DESC")
    fun searchByTitleArticlesWithLimitTimelineOrderView(
        pageable: Pageable,
        @Param("nowmillis") nowmillis: Long,
        @Param("diffmillis") diffmillis: Long,
        @Param("titlesearch") titlesearch: String
    ): Page<Article>

    @Query("SELECT a FROM Article a where :nowmillis - a.date <= :diffmillis and LOWER(a.title) LIKE %:titlesearch% ORDER BY a.view ASC, a.date DESC")
    fun searchByTitleArticlesWithLimitTimelineOrderViewASC(
        pageable: Pageable,
        @Param("nowmillis") nowmillis: Long,
        @Param("diffmillis") diffmillis: Long,
        @Param("titlesearch") titlesearch: String
    ): Page<Article>

    @Query("SELECT a FROM Article a where :nowmillis - a.date <= :diffmillis and LOWER(a.title) LIKE %:titlesearch% ORDER BY a.likes DESC, a.date DESC")
    fun searchByTitleArticlesWithLimitTimelineOrderLike(
        pageable: Pageable,
        @Param("nowmillis") nowmillis: Long,
        @Param("diffmillis") diffmillis: Long,
        @Param("titlesearch") titlesearch: String
    ): Page<Article>


    @Query("SELECT a FROM Article a where :nowmillis - a.date <= :diffmillis and LOWER(a.title) LIKE %:titlesearch% ORDER BY a.likes ASC, a.date DESC")
    fun searchByTitleArticlesWithLimitTimelineOrderLikeASC(
        pageable: Pageable,
        @Param("nowmillis") nowmillis: Long,
        @Param("diffmillis") diffmillis: Long,
        @Param("titlesearch") titlesearch: String
    ): Page<Article>
}
