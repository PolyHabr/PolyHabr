package com.polyhabr.poly_back.controller.model.user.request

import com.polyhabr.poly_back.dto.UserDto
import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Schema(
    name = "UserRequest",
    description = "Data object for User Request",
)
data class UserRequest(

    @field:Schema(
        example = "polyhabr@email.com",
        pattern = "^[A-Za-z0-9+_.-]+@(.+)\$"
    )
    @field:NotBlank
    @field:NotNull
    @field:Size(min = 3, max = 50)
    @field:Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)\$")
    var email: String? = null,

    @field:Schema(example = "polyhabr", pattern = "^[A-Za-z0-9]+\$")
    @field:NotBlank
    @field:NotNull
    @field:Size(min = 5, max = 20)
    @field:Pattern(regexp = "^[A-Za-z0-9]+\$")
    var login: String? = null,

    @field:Schema(example = "polyhabr123", pattern = "^[A-Za-z0-9]+\$")
    @field:NotBlank
    @field:NotNull
    @field:Size(min = 8, max = 35)
    @field:Pattern(regexp = "^[A-Za-z0-9]+\$")
    var password: String? = null,

    @field:Schema(example = "Ivan", pattern = "^[A-Za-z]+\$")
    @field:NotBlank
    @field:NotNull
    @field:Size(min = 2, max = 15)
    @field:Pattern(regexp = "^[A-Za-z]+\$")
    var name: String? = null,

    @field:Schema(example = "Trueman", pattern = "^[A-Za-z]+\$")
    @field:NotBlank
    @field:NotNull
    @field:Size(min = 2, max = 15)
    @field:Pattern(regexp = "^[A-Za-z]+\$")
    var surname: String? = null,
) : Serializable

fun UserRequest.toDto() = UserDto(
    email = this.email!!,
    login = this.login!!,
    name = this.name!!,
    password = this.password!!,
    surname = this.surname!!,
)
