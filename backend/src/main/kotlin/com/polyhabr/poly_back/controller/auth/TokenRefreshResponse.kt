package com.polyhabr.poly_back.controller.auth

data class TokenRefreshResponse(
    val accessToken: String,
    val refreshToken: String
)
