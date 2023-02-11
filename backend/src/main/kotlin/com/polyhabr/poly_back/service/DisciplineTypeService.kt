package com.polyhabr.poly_back.service

import com.polyhabr.poly_back.dto.DisciplineTypeDto

interface DisciplineTypeService {
    fun getAll(): List<DisciplineTypeDto>

}