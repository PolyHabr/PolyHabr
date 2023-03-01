package com.polyhabr.poly_back.controller.model.file

import com.fasterxml.jackson.annotation.JsonIgnore
import com.polyhabr.poly_back.dto.FileCreatingDto
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.web.multipart.MultipartFile
import java.io.Serializable

@Schema(
    name = "FileRequest",
    description = "Data object for File Request",
)
data class FileRequest(
    val articleId: Long,
    val file: MultipartFile
) : Serializable
