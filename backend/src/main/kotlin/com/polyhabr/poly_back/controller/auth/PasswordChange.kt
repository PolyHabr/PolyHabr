package com.polyhabr.poly_back.controller.auth

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Schema(
    name = "PasswordChange",
)
data class PasswordChange(
    @field:NotBlank
    @field:NotNull
    @JsonProperty("token")
    var token: String? = null,

    @field:Schema(example = "polyhabr123", pattern = "^[A-Za-z0-9]+\$")
    @field:NotBlank
    @field:NotNull
    @field:Size(min = 8, max = 35)
    @field:Pattern(regexp = "^[A-Za-z0-9]+\$")
    val newPassword: String? = null
) : Serializable
