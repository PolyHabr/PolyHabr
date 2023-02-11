package com.polyhabr.poly_back.repository

import com.polyhabr.poly_back.entity.DisciplineType
import org.springframework.data.jpa.repository.JpaRepository

interface DisciplineTypeRepository: JpaRepository<DisciplineType, Long>
