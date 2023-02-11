package com.polyhabr.poly_back.controller.model.user.response

data class UserCreateResponse(
    val isSuccess: Boolean,
    val id: Long? = null,
)