package com.polyhabr.poly_back.controller

import com.polyhabr.poly_back.dto.DisciplineTypeDto
import com.polyhabr.poly_back.service.DisciplineTypeService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/discipline_type")
class DisciplineTypeContrroller(
    private val disciplineTypeService: DisciplineTypeService,
) {
    @GetMapping
    fun getAll(): List<DisciplineTypeDto> = disciplineTypeService.getAll()
}