package com.polyhabr.poly_back.controller.model.tagType.request

import com.polyhabr.poly_back.dto.TagTypeDto
import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotNull

@Schema(
    name = "TagTypeRequest",
    description = "Data object for Tag Type",
)
data class TagTypeRequest(
    @field:Schema(example = "#math")
    @field:NotNull
    var name: String? = null,
)

fun TagTypeRequest.toDto()= TagTypeDto(
    name = this.name!!,
)