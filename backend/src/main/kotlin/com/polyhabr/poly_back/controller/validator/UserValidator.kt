package com.polyhabr.poly_back.controller.validator

import com.polyhabr.poly_back.dto.UserDto


object UserValidator {
    fun validateEmail(email: String?): Boolean {
        val regex = Regex("^[A-Za-z0-9+_.-]+@(.+)\$")
        return email?.let { regex.matches(it) } ?: false
    }

    fun validateName(name: String?): Boolean {
        val regex = Regex("^[A-Za-z]+\$")
        return name?.let { regex.matches(it) } ?: false
    }

    fun validateSurname(surname: String?): Boolean {
        val regex = Regex("^[A-Za-z]+\$")
        return surname?.let { regex.matches(it) } ?: false
    }

    fun validatePassword(password: String?): Boolean {
        val regex = Regex("^[A-Za-z0-9]+\$")
        return password?.let { regex.matches(it) } ?: false
    }

    fun validateLogin(login: String?): Boolean {
        val regex = Regex("^[A-Za-z0-9]+\$")
        return login?.let { regex.matches(it) } ?: false
    }

    fun validate(user: UserDto?): Boolean {
        if (user == null) return false
        return validateEmail(user.email) &&
                validateName(user.name) &&
                validateSurname(user.surname) &&
                validatePassword(user.password) &&
                validateLogin(user.login)
    }
}