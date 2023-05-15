package com.polyhabr.poly_back.service

import com.polyhabr.poly_back.entity.RefreshToken
import com.polyhabr.poly_back.exception.TokenRefreshException
import com.polyhabr.poly_back.repository.RefreshTokenRepository
import com.polyhabr.poly_back.repository.auth.UsersRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*


@Service
class RefreshTokenService(
    private val usersRepository: UsersRepository,
    private val refreshTokenRepository: RefreshTokenRepository
) {
    private val refreshTokenDurationMs: Long = 2600000L * 1000 // 30 дней
    fun findByToken(token: String?): Optional<RefreshToken> {
        return refreshTokenRepository.findByToken(token)
    }

    fun createRefreshToken(userId: Long): RefreshToken? {
        var refreshToken = RefreshToken()
        refreshToken.user = usersRepository.findById(userId).get()
        refreshToken.expiryDate = Instant.now().plusMillis(refreshTokenDurationMs)
        refreshToken.token = UUID.randomUUID().toString()
        refreshToken = refreshTokenRepository.save(refreshToken)
        return refreshToken
    }

    fun verifyExpiration(token: RefreshToken): RefreshToken? {
        if (token.expiryDate!! < Instant.now()) {
            refreshTokenRepository.delete(token)
            throw TokenRefreshException(token.token, "Refresh token was expired. Please make a new signin request")
        }
        return token
    }
}