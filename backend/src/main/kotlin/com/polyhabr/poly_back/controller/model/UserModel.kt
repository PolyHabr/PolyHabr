package com.polyhabr.poly_back.controller.model

import com.polyhabr.poly_back.dto.UserDto

data class UserModel(
    var email: String? = null,
    var login: String? = null,
    var password: String? = null,
    var name: String? = null,
    var surname: String? = null,
)

fun UserModel.toDto() = UserDto(
    email = this.email!!,
    login = this.login!!,
    name = this.name!!,
    password = this.password!!,
    surname = this.surname!!,
)
