package com.polyhabr.poly_back.controller.model.file

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.web.multipart.MultipartFile
import java.io.Serializable

@Schema(
    name = "PicRequest",
    description = "Data object forPicRequest",
)
data class PicRequest(
    val articleId: Long,
    val file: MultipartFile
) : Serializable
