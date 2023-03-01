package com.polyhabr.poly_back.entity

import com.polyhabr.poly_back.entity.auth.User
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.sql.Timestamp
import java.time.LocalDate
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "article")
open class Article(

    @Column(name = "title", length = 100)
    open var title: String,

    @Column(name = "text", length = 10000)
    open var text: String,

    @Column(name = "preview_text", length = 400)
    open var previewText: String,

    @Column(name = "date")
    open var date: Long,

    @Column(name = "file_pdf")
    @Deprecated("dont use this field")
    open var filePdf: String? = null,

    @NotNull
    @Column(name = "likes", nullable = false)
    open var likes: Int = 0,

    @NotNull
    @Column(name = "is_fav", nullable = false)
    open var isFav: Boolean = false,

    @NotNull
    @Column(name = "view", nullable = false)
    open var view: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "type_id")
    open var typeId: ArticleType?,

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    open var userId: User?,

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "file_id")
    open var file_id: File? = null
) : BaseEntity<Long>()
