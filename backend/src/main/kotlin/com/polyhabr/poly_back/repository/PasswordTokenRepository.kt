package com.polyhabr.poly_back.repository

import com.polyhabr.poly_back.entity.PasswordResetToken
import org.springframework.data.jpa.repository.JpaRepository

interface PasswordTokenRepository : JpaRepository<PasswordResetToken, Long> {
    fun findByToken(token: String?): PasswordResetToken?
}