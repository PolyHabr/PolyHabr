package com.polyhabr.poly_back.controller.auth

import org.springframework.security.core.GrantedAuthority

class JwtResponse(
    var accessToken: String?,
    var username: String?,
    val authorities: Collection<GrantedAuthority>,
    val isFirst: Boolean
) {
    var type = "Bearer"
}