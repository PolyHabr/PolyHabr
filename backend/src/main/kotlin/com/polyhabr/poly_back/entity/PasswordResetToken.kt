package com.polyhabr.poly_back.entity

import com.polyhabr.poly_back.entity.auth.User
import java.util.Date
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.OneToOne


@Entity
open class PasswordResetToken : BaseEntity<Long>() {
    companion object {
        private const val EXPIRATION = 60 * 24
    }

    private var token: String? = null

    @OneToOne(targetEntity = User::class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private var user: User? = null

    private var expiryDate: Date? = null
}