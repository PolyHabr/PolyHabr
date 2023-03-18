package com.polyhabr.poly_back.entity

import com.polyhabr.poly_back.dto.ArticleDto
import com.polyhabr.poly_back.entity.auth.User
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.joda.time.DateTime
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

    @Column(name = "preview_text", length = 1000)
    open var previewText: String,

    @Column(name = "date")
    open var date: Long,

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
    open var file_id: File? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "preview_src_id")
    open var preview_src_id: File? = null,
) : BaseEntity<Long>()

fun Article.toDto(
    disciplineList: List<String>,
    tagList: List<String>,
): ArticleDto =
    ArticleDto(
        id = this.id,
        date = DateTime(this.date),
        likes = this.likes,
        previewText = this.previewText,
        typeId = this.typeId,
        userId = this.userId,
        title = this.title,
        listDisciplineName = disciplineList,
        listTag = tagList,
        text = this.text,
        fileId = this.file_id?.id,
        viewCount = this.view,
        isSaveToFavourite = isFav,
        pdfId = file_id?.id,
        previewImgId = preview_src_id?.id
    )
