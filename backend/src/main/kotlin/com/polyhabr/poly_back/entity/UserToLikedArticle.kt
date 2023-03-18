package com.polyhabr.poly_back.entity

import com.polyhabr.poly_back.controller.model.userToLikedArticle.request.UserToLikedArticleRequest
import com.polyhabr.poly_back.dto.UserToLikedArticleDto
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import com.polyhabr.poly_back.entity.auth.User
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "user_to_liked_article")
open class UserToLikedArticle(
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "article_id", nullable = false)
    open var article: Article? = null,

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    open var user: User? = null
) : BaseEntity<Long>()

fun UserToLikedArticle.toDto() = UserToLikedArticleDto(
    id = this.id,
    articleId = this.article,
    userId = this.user,
)
