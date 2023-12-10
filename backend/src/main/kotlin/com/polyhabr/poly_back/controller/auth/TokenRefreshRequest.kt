package com.polyhabr.poly_back.controller.auth

data class TokenRefreshRequest(
    val refreshToken: String
)
