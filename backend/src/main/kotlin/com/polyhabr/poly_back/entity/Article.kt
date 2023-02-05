package com.polyhabr.poly_back.entity

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
    @JoinColumn(name = "type_id")
    open var type: ArticleType,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    open var user: User
) : BaseEntity<Long>()
