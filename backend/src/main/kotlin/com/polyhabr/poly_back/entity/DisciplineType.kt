package com.polyhabr.poly_back.entity

import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "discipline_type")
open class DisciplineType() : BaseEntity<Long>() {
    @Size(max = 255)
    @Column(name = "name")
    open var name: String? = null

    constructor(name: String) : this() {
        this.name = name
    }
}
