package com.polyhabr.poly_back.dto

import com.polyhabr.poly_back.entity.auth.User

data class UserDto(
    val id: Long? = null,
    var email: String? = null,
    var login: String? = null,
    var password: String? = null,
    var name: String? = null,
    var surname: String? = null,
)

fun UserDto.toEntity() = User(
    email = this.email!!,
    login = this.login!!,
    name = this.name!!,
    password = this.password!!,
    surname = this.surname!!,
)
