package com.polyhabr.poly_back.controller.model.disciplineType.response

import io.swagger.v3.oas.annotations.media.Schema

data class DisciplineTypeSimpleListResponse(
    @field:Schema(description = "List of discipline types")
    val contents: List<DisciplineTypeResponse>,
)
