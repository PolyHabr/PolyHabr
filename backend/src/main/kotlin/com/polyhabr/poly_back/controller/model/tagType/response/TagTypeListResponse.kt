package com.polyhabr.poly_back.controller.model.tagType.response

import com.polyhabr.poly_back.dto.TagTypeDto

data class TagTypeListResponse(
    val tagTypes: List<TagTypeResponse>,
    val page: Int,
)

fun List<TagTypeDto>.toResponse(): TagTypeListResponse{
    return TagTypeListResponse(
        tagTypes = this.map {it.toResponse()},
        page = 0, // todo
    )
}