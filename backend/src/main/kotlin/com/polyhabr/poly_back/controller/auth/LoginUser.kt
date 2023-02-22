package com.polyhabr.poly_back.controller.auth

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

class LoginUser : Serializable {

    @field:Schema(example = "polyhabr", pattern = "^[A-Za-z0-9]+\$")
    @field:NotBlank
    @field:NotNull
    @field:Size(min = 5, max = 20)
    @field:Pattern(regexp = "^[A-Za-z0-9]+\$")
    @JsonProperty("username")
    var username: String? = null

    @field:Schema(example = "polyhabr123", pattern = "^[A-Za-z0-9]+\$")
    @field:NotBlank
    @field:NotNull
    @field:Size(min = 8, max = 35)
    @field:Pattern(regexp = "^[A-Za-z0-9]+\$")
    @JsonProperty("password")
    var password: String? = null

    constructor() {}

    constructor(username: String, password: String) {
        this.username = username
        this.password = password
    }

    companion object {
        private const val serialVersionUID = -1764970284520387975L
    }
}
