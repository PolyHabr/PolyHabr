package com.polyhabr.poly_back.service

import com.polyhabr.poly_back.controller.model.disciplineType.request.DisciplineTypeRequest
import com.polyhabr.poly_back.controller.model.disciplineType.request.UpdateMyDisciplineRequest
import com.polyhabr.poly_back.dto.DisciplineTypeDto
import org.springframework.data.domain.Page

interface DisciplineTypeService {
    fun getAll(offset: Int, size: Int): Page<DisciplineTypeDto>

    fun getById(id: Long): DisciplineTypeDto

    fun searchByName(prefix: String?, offset: Int, size: Int): Page<DisciplineTypeDto>

    fun create(disciplineTypeRequest: DisciplineTypeRequest): Long?

    fun update(id: Long, disciplineTypeRequest: DisciplineTypeRequest): Pair<Boolean, String>

    fun delete(id: Long): Pair<Boolean, String>
    fun getMy(): List<DisciplineTypeDto>
    fun updateMy(updateResponse: UpdateMyDisciplineRequest): Boolean
}