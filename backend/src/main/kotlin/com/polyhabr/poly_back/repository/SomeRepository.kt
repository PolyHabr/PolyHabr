package com.polyhabr.poly_back.repository

import com.polyhabr.poly_back.repository.model.*
import org.springframework.stereotype.Repository

@Repository
open class SomeRepository {
    private val listUsers: List<User> = mutableListOf(
        User()
    )
    private val listArticles: List<Article> = mutableListOf(
        Article()
    )
    private val listComments: List<Comment> = mutableListOf(
        Comment()
    )
    private val listArticleType: List<ArticleType> = mutableListOf(
        ArticleType()
    )
    private val listDisciplineType: List<DisciplineType> = mutableListOf(
        DisciplineType()
    )
    private val listTagType: List<TagType> = mutableListOf(
        TagType()
    )
    private val userToLikeArticle: Map<Int, List<Int>> = mutableMapOf(
        0 to listOf(0)
    )
    private val articleToDisciplineType: Map<Int, List<Int>> = mutableMapOf(
        0 to listOf(0)
    )
    private val articleToTagType: Map<Int, List<Int>> = mutableMapOf(
        0 to listOf(0)
    )

    fun getUsers(): List<User> {
        return listUsers
    }

    fun getArticles(): List<Article> {
        return listArticles
    }

    fun getComments(): List<Comment> {
        return listComments
    }

    fun getArticleType(): List<ArticleType> {
        return listArticleType
    }

    fun getDisciplineType(): List<DisciplineType> {
        return listDisciplineType
    }

    fun getTagType(): List<TagType> {
        return listTagType
    }

    fun getUserToLikeArticle(): Map<Int, List<Int>> {
        return userToLikeArticle
    }

    fun getArticleToDisciplineType(): Map<Int, List<Int>> {
        return articleToDisciplineType
    }

    fun getArticleToTagType(): Map<Int, List<Int>> {
        return articleToTagType
    }

    fun likeArticle(userId: Int, articleId: Int) {
        (userToLikeArticle[userId] as MutableList).add(articleId)
    }

    fun addArticle(article: Article) {
        (listArticles as MutableList).add(article)
    }

    fun addComment(comment: Comment) {
        (listComments as MutableList).add(comment)
    }
}