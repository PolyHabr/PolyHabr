package com.polyhabr.poly_back.controller.model.disciplineType.request

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotNull

data class UpdateMyDisciplineRequest(
    @field:Schema(example = "math", description = "should be swap list")
    @field:NotNull
    var namesDiscipline: List<String>? = null
)
