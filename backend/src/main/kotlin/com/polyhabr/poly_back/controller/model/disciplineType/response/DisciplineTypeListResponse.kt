package com.polyhabr.poly_back.controller.model.disciplineType.response

import com.polyhabr.poly_back.dto.DisciplineTypeDto

data class DisciplineTypeListResponse(
    val discipleTypes: List<DisciplineTypeResponse>,
    val page: Int,
)

fun List<DisciplineTypeDto>.toResponse(): DisciplineTypeListResponse {
    return DisciplineTypeListResponse(
        discipleTypes = this.map {it.toResponse()},
        page = 0, // todo
    )
}