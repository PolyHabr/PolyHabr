package com.polyhabr.poly_back.entity

import com.polyhabr.poly_back.dto.ArticleToDisciplineTypeDto
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "article_to_discipline_type")
open class ArticleToDisciplineType(
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "article_id", nullable = false)
    open var article: Article? = null,

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "discipline_type_id", nullable = false)
    open var disciplineType: DisciplineType? = null
) : BaseEntity<Long>()

fun ArticleToDisciplineType.toDto() = ArticleToDisciplineTypeDto(
    id = this.id,
    articleId = this.article,
    disciplineTypeId = this.disciplineType,
)
