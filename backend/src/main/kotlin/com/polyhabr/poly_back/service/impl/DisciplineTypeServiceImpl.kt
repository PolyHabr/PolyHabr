package com.polyhabr.poly_back.service.impl

import com.polyhabr.poly_back.dto.DisciplineTypeDto
import com.polyhabr.poly_back.entity.DisciplineType
import com.polyhabr.poly_back.repository.DisciplineTypeRepository
import com.polyhabr.poly_back.service.DisciplineTypeService
import org.springframework.stereotype.Service

@Service
class DisciplineTypeServiceImpl(
    private val disciplineTypeRepository: DisciplineTypeRepository
) : DisciplineTypeService {
    override fun getAll(): List<DisciplineTypeDto> {
        return disciplineTypeRepository.findAll().map{
            it.toDto()
        }
    }

    private fun DisciplineType.toDto(): DisciplineTypeDto =
        DisciplineTypeDto(
            id = this.id,
            name = this.name,
        )
}