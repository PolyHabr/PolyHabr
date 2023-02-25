package com.polyhabr.poly_back.controller.model.disciplineType.request

import com.polyhabr.poly_back.dto.DisciplineTypeDto
import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotNull

@Schema(
    name = "DisciplineTypeRequest",
    description = "Data object for Discipline Type",
)
data class DisciplineTypeRequest(
    @field:Schema(example = "math")
    @field:NotNull
    var name: String? = null,
)

fun DisciplineTypeRequest.toDto()= DisciplineTypeDto(
    name = this.name!!,
)