package com.polyhabr.poly_back.repository.auth

import com.polyhabr.poly_back.entity.auth.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*
import javax.transaction.Transactional

interface UsersRepository : JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.name LIKE %:pname%")
    fun findUsersByName(pageable: Pageable, @Param("pname") pname: String): Page<User>

    @Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
    fun findByVerificationCode(code: String): User?

    fun existsByLogin(@Param("login") login: String): Boolean

    fun findByLogin(@Param("login") login: String): User?

    fun findByEmail(@Param("email") email: String): User?

    fun findByResetToken(@Param("reset_token") resetToken: String): User?

    @Transactional
    fun deleteByLogin(@Param("login") login: String)
}
