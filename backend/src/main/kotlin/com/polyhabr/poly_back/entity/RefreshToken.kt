package com.polyhabr.poly_back.entity

import com.polyhabr.poly_back.entity.auth.User
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.OneToOne

@Entity(name = "refreshtoken")
data class RefreshToken(
    open var token: String? = null,

    @OneToOne(targetEntity = User::class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    open var user: User? = null,

    open var expiryDate: java.time.Instant? = null
) : BaseEntity<Long>()

