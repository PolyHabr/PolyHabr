package com.polyhabr.poly_back.entity

import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "users")
open class User(
    @Size(max = 255)
    @Column(name = "login")
    open var login: String,

    @Size(max = 255)
    @Column(name = "password")
    open var password: String,

    @Size(max = 255)
    @Column(name = "name")
    open var name: String,

    @Size(max = 255)
    @Column(name = "surname")
    open var surname: String,

    @Size(max = 255)
    @Column(name = "email")
    open var email: String
) : BaseEntity<Long>()
