package com.polyhabr.poly_back.controller.model.disciplineType.response

import com.polyhabr.poly_back.dto.DisciplineTypeDto

data class DisciplineTypeResponse(
    var name: String,
)

fun DisciplineTypeDto.toResponse(): DisciplineTypeResponse {
    return DisciplineTypeResponse(
        name = this.name,
    )
}