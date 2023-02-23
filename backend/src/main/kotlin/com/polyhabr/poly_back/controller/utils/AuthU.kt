package com.polyhabr.poly_back.controller.utils

import org.springframework.security.core.context.SecurityContextHolder

fun currentLogin(): String {
    return SecurityContextHolder.getContext().authentication.name
}