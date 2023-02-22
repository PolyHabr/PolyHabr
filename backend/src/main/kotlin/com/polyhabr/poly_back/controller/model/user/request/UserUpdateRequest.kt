package com.polyhabr.poly_back.controller.model.user.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.polyhabr.poly_back.dto.UserDto
import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Schema(
    name = "UserUpdateRequest",
    description = "Data object for User Update Request",
)
data class UserUpdateRequest(
    @field:Schema(
        example = "polyhabr@email.com",
        pattern = "^[A-Za-z0-9+_.-]+@(.+)\$"
    )
    @field:Size(min = 3, max = 50)
    @field:Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)\$")
    var email: String? = null,

    @field:Schema(example = "Ivan", pattern = "^[A-Za-z]+\$")
    @field:Size(min = 2, max = 15)
    @field:Pattern(regexp = "^[A-Za-z]+\$")
    var name: String? = null,

    @field:Schema(example = "Trueman", pattern = "^[A-Za-z]+\$")
    @field:Size(min = 2, max = 15)
    @field:Pattern(regexp = "^[A-Za-z]+\$")
    var surname: String? = null,

    @field:Schema(example = "polyhabr123", pattern = "^[A-Za-z0-9]+\$")
    @field:Size(min = 8, max = 35)
    @field:Pattern(regexp = "^[A-Za-z0-9]+\$")
    @JsonProperty("password")
    var password: String? = null
) : Serializable
