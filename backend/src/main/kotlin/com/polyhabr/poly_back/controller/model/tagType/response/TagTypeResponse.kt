package com.polyhabr.poly_back.controller.model.tagType.response

import com.polyhabr.poly_back.dto.TagTypeDto

data class TagTypeResponse(
    var name: String,
)

fun TagTypeDto.toResponse(): TagTypeResponse{
    return TagTypeResponse(
        name = this.name!!,
    )
}