package com.polyhabr.poly_back.entity

import com.polyhabr.poly_back.entity.auth.User
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "comment")
open class Comment : BaseEntity<Long>() {

    @Size(max = 255)
    @Column(name = "text")
    open var text: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    open var article: Article? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    open var user: User? = null
}
