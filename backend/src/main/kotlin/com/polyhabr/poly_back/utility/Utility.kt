package com.polyhabr.poly_back.utility

import javax.servlet.http.HttpServletRequest

object Utility {
    fun getSiteURL(request: HttpServletRequest): String {
        val siteURL = request.requestURL.toString()
        return siteURL.replace(request.servletPath, "")
    }
}