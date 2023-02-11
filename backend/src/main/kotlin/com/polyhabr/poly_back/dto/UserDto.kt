package com.polyhabr.poly_back.dto

data class UserDto(
    val id: Long? = null,
    var email: String? = null,
    var login: String? = null,
    var password: String? = null,
    var name: String? = null,
    var surname: String? = null,
)
