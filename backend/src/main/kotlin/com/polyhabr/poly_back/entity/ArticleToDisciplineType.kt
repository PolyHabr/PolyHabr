package com.polyhabr.poly_back.entity

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "article_to_discipline_type")
open class ArticleToDisciplineType : BaseEntity<Long>() {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "article_id", nullable = false)
    open var article: Article? = null

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "discipline_type_id", nullable = false)
    open var disciplineType: DisciplineType? = null
}
