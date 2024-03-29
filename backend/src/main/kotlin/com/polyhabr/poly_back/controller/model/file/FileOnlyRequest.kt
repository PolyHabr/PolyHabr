package com.polyhabr.poly_back.controller.model.file

import com.fasterxml.jackson.annotation.JsonIgnore
import com.polyhabr.poly_back.dto.FileCreatingDto
import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable

@Schema
data class FileOnlyRequest(
    var name: String? = null,
    var description: String? = null,
    var originalName: String? = null,
    @field:JsonIgnore
    var data: ByteArray? = null,
) : Serializable

fun FileOnlyRequest.toDto(): FileCreatingDto {
    return FileCreatingDto(
        name = this.name,
        description = this.description,
        data = this.data,
        originalName = this.originalName
    )
}
