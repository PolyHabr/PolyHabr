package com.polyhabr.poly_back.controller.utils

import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable
import javax.validation.constraints.NotNull

@Schema(
    name = "FieldRequest",
    description = "Data object for FieldRequest"
)
data class FieldRequest(
    @field:Schema(example = "field, not null, default = empty")
    @field:NotNull
    var nameField: String = "",

    @field:Schema(example = "isAsc, not null, default = true")
    @field:NotNull
    var isAsc: Boolean = true,


    @field:Schema(example = "forDataRange, nullable, 1w, 1m, 1y, all, default = all")
    @field:NotNull
    var forDataRange: String? = "all"
) : Serializable
