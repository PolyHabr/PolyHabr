package com.polyhabr.poly_back.entity

import com.polyhabr.poly_back.dto.TagTypeDto
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "tag_type")
open class TagType() : BaseEntity<Long>() {
    @Size(max = 255)
    @Column(name = "name")
    open var name: String? = null

    constructor(name: String) : this() {
        this.name = name
    }
}

fun TagType.toDto(): TagTypeDto =
    TagTypeDto(
        id = this.id!!,
        name = this.name!!,
    )
