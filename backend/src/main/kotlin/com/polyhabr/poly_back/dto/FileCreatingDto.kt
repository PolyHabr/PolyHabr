package com.polyhabr.poly_back.dto

import javax.validation.constraints.NotBlank
import com.fasterxml.jackson.annotation.JsonIgnore;


class FileCreatingDto(
    @field:NotBlank
    var username: String? = null,

    var name: String? = null,
    var originalName: String? = null,
    var description: String? = null,

    @field:JsonIgnore
    var data: ByteArray? = null,
)