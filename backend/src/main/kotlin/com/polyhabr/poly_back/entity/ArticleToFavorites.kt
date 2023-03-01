package com.polyhabr.poly_back.entity

import com.polyhabr.poly_back.entity.auth.User
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "article_to_fav")
open class ArticleToFav(
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    @JoinColumn(name = "article_id")
    open var articleId: Article? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    open var userId: User? = null,
) : BaseEntity<Long>()