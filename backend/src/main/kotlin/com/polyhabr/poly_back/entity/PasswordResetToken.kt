package com.polyhabr.poly_back.entity

import com.polyhabr.poly_back.entity.auth.User
import java.util.Date
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.OneToOne


@Entity
open class PasswordResetToken(
    open var token: String? = null,

    @OneToOne(targetEntity = User::class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    open var user: User? = null,

    open var expiryDate: Date? = null
) : BaseEntity<Long>() {
    companion object {
        const val FIVE_MIN_EXPIRATION_IN_MILIS = 5 * 60 * 1000
    }
}