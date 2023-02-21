package com.polyhabr.poly_back.entity

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDate
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "article")
open class Article(
    @Column(name = "date")
    open var date: LocalDate,

    @Size(max = 255)
    @Column(name = "file_pdf")
    open var filePdf: String? = null,

    @NotNull
    @Column(name = "likes", nullable = false)
    open var likes: Int = 0,

    @Size(max = 255)
    @Column(name = "preview_text")
    open var previewText: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "type_id")
    open var typeId: ArticleType?,

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    open var userId: User?
) : BaseEntity<Long>()
