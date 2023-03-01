package com.polyhabr.poly_back.utility

import javax.servlet.http.HttpServletRequest

object Utility {
    fun getSiteURL(request: HttpServletRequest): String {
        val siteURL = request.requestURL.toString()
        return siteURL.replace(request.servletPath, "")
    }

    fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}