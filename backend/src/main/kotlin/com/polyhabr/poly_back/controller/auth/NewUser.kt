package com.polyhabr.poly_back.controller.auth

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Schema(
    name = "NewUser",
)
class NewUser : Serializable {

    @field:Schema(example = "polyhabr", pattern = "^[A-Za-z0-9]+\$")
    @field:NotBlank
    @field:NotNull
    @field:Size(min = 3, max = 20)
    @field:Pattern(regexp = "^[A-Za-z0-9]+\$")
    @JsonProperty("username")
    var username: String? = null

    @field:Schema(example = "Ivan", pattern = "^[A-Za-z]+\$")
    @field:NotBlank
    @field:NotNull
    @field:Size(min = 2, max = 15)
    @field:Pattern(regexp = "^[A-Za-z]+\$")
    @JsonProperty("firstName")
    var firstName: String? = null

    @field:Schema(example = "Trueman", pattern = "^[A-Za-z]+\$")
    @field:NotBlank
    @field:NotNull
    @field:Size(min = 2, max = 15)
    @field:Pattern(regexp = "^[A-Za-z]+\$")
    @JsonProperty("lastName")
    var lastName: String? = null

    @field:Schema(
        example = "polyhabr@email.com",
        pattern = "^[A-Za-z0-9+_.-]+@(.+)\$"
    )
    @field:NotBlank
    @field:NotNull
    @field:Size(min = 3, max = 50)
    @field:Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)\$")
    @JsonProperty("email")
    var email: String? = null

    @field:Schema(example = "polyhabr123", pattern = "^[A-Za-z0-9]+\$")
    @field:NotBlank
    @field:NotNull
    @field:Size(min = 8, max = 35)
    @field:Pattern(regexp = "^[A-Za-z0-9]+\$")
    @JsonProperty("password")
    var password: String? = null

    constructor() {}

    constructor(
        username: String,
        firstName: String,
        lastName: String,
        email: String,
        password: String,
    ) {
        this.username = username
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
        this.password = password
    }

    companion object {
        private const val serialVersionUID = -1764970284520387975L
    }
}