package com.polyhabr.poly_back.entity

import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor
import javax.persistence.*

@Entity
@Table(name = "user")
class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    var email: String,
    var login: String,
    var password: String,
    var name: String,
    var surname: String ?= null

)