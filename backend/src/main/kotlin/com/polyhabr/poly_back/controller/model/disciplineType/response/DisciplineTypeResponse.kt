package com.polyhabr.poly_back.controller.model.disciplineType.response

import com.polyhabr.poly_back.dto.DisciplineTypeDto

data class DisciplineTypeResponse(
    var id: Long,
    var name: String,
)

fun DisciplineTypeDto.toResponse(): DisciplineTypeResponse {
    return DisciplineTypeResponse(
        id = this.id,
        name = this.name,
    )
}