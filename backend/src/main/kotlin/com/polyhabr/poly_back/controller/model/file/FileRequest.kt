package com.polyhabr.poly_back.controller.model.file

import com.fasterxml.jackson.annotation.JsonIgnore
import com.polyhabr.poly_back.dto.FileCreatingDto
import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "FileRequest",
    description = "Data object for File Request",
)
data class FileRequest(
    var name: String? = null,
    var originalName: String? = null,
    var description: String? = null,

    @field:JsonIgnore
    var data: ByteArray? = null,
)

fun FileRequest.toDto(): FileCreatingDto {
    return FileCreatingDto(
        name = this.name,
        description = this.description,
        data = this.data,
        originalName = this.originalName
    )
}
