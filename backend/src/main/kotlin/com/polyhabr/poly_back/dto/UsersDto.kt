package com.polyhabr.poly_back.dto

data class UsersDto(
    val id: Int? = null,
    var email: String,
    var login: String,
    var password: String,
    var name: String,
    var surname: String,
)
