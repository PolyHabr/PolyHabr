package com.polyhabr.poly_back.controller.model.file

import com.fasterxml.jackson.annotation.JsonIgnore
import com.polyhabr.poly_back.dto.FileCreatingDto
import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable

@Schema(
    name = "FileRequest",
    description = "Data object for File Request",
)
data class FileRequest(
    var name: String? = null,
    var description: String? = null,
) : Serializable

fun FileRequest.toDto(data: ByteArray?, originName: String?): FileCreatingDto {
    return FileCreatingDto(
        name = this.name,
        description = this.description,
        data = data,
        originalName = originName
    )
}
