package com.polyhabr.poly_back.repository

import com.polyhabr.poly_back.entity.RefreshToken
import com.polyhabr.poly_back.entity.auth.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository
import java.util.Optional


@Repository
interface RefreshTokenRepository : JpaRepository<RefreshToken, Long> {
    fun findByToken(token: String?): Optional<RefreshToken>
}