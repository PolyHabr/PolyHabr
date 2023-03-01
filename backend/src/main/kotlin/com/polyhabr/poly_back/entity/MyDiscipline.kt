package com.polyhabr.poly_back.entity

import com.polyhabr.poly_back.entity.auth.User
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*

@Entity
@Table(name = "my_discipline")
open class MyDiscipline(
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "discipline_id")
    open var disciplineId: DisciplineType? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    open var userId: User? = null
) : BaseEntity<Long>() {

}