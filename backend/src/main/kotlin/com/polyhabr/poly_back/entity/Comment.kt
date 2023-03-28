package com.polyhabr.poly_back.entity

import com.polyhabr.poly_back.dto.CommentDto
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import com.polyhabr.poly_back.entity.auth.User
import org.joda.time.DateTime
import java.sql.Timestamp
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "comment")
open class Comment(
    @Size(max = 255)
    @Column(name = "text")
    open var text: String? = null,

    @Column(name = "date")
    open var date: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "article_id")
    open var articleId: Article? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    open var userId: User? = null,
) : BaseEntity<Long>()

fun Comment.toDto(): CommentDto =
    CommentDto(
        id = this.id,
        text = this.text,
        articleId = this.articleId,
        userId = this.userId,
        data = DateTime(date)
    )
