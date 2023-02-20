package com.polyhabr.poly_back.entity.auth

import com.polyhabr.poly_back.entity.BaseEntity

import javax.persistence.*

@Entity
@Table(name = "roles")
data class Role(
    @Column(name = "name")
    val name: String
) : BaseEntity<Long>()