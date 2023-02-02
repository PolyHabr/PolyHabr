package com.polyhabr.poly_back.service.impl

import com.polyhabr.poly_back.dto.DisciplineTypeDto
import com.polyhabr.poly_back.service.DisciplineTypeService
import org.springframework.stereotype.Service

@Service
class DisciplineTypeServiceImpl : DisciplineTypeService {
    override fun getAll(): List<DisciplineTypeDto> {
        return listOf(
            DisciplineTypeDto(1)
        )
    }
}