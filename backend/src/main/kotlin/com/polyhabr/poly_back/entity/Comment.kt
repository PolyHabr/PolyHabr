package com.polyhabr.poly_back.entity

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import com.polyhabr.poly_back.entity.auth.User
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "comment")
open class Comment() : BaseEntity<Long>() {

    @Size(max = 255)
    @Column(name = "text")
    open var text: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "article_id")
    open var articleId: Article? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    open var userId: User? = null

    constructor(text: String, articleId: Article, userId: User) : this() {
        this.text = text
        this.articleId = articleId
        this.userId = userId
    }
}
