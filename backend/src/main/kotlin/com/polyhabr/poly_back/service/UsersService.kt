package com.polyhabr.poly_back.service

import com.polyhabr.poly_back.controller.auth.PasswordChange
import com.polyhabr.poly_back.controller.model.user.request.UserRequest
import com.polyhabr.poly_back.controller.model.user.request.UserUpdateRequest
import com.polyhabr.poly_back.dto.UserDto
import com.polyhabr.poly_back.entity.auth.User
import org.springframework.data.domain.Page
import javax.validation.constraints.Email

interface UsersService {
    fun getAll(offset: Int, size: Int): Page<UserDto>

    fun getById(id: Long): UserDto?

    fun searchByName(prefix: String?, offset: Int, size: Int): Page<UserDto>

    fun create(userRequest: UserRequest): Long?

    fun update(userRequest: UserUpdateRequest): Pair<Boolean, String>

    fun delete(): Pair<Boolean, String>

    fun sendVerificationEmail(user: User, siteURL: String = "http://localhost:8733")

    fun sendResetPasswordEmail(email: String, siteURL: String = "http://localhost:8733")

    fun verify(verificationCode: String): Boolean

    fun validatePasswordResetToken(token: String?): Boolean
    fun changeUserPassword(passwordChange: PasswordChange): Boolean
}