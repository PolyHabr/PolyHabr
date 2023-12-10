package com.polyhabr.poly_back.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
class TokenRefreshException(
    token: String?,
    message: String?
) : RuntimeException(String.format("Failed for [%s]: %s", token, message)) {
    private val serialVersionUID = 1L
}