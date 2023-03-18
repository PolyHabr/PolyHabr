package com.polyhabr.poly_back.controller.utils

import org.springframework.security.core.context.SecurityContextHolder

fun currentLogin(): String? {
    return try {
        SecurityContextHolder.getContext().authentication.name
    } catch (e: Exception) {
        null
    }
}