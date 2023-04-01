package com.polyhabr.poly_back.repository

import com.polyhabr.poly_back.entity.DisciplineType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface DisciplineTypeRepository : JpaRepository<DisciplineType, Long> {
    @Query("SELECT dt FROM DisciplineType dt WHERE LOWER(dt.name) LIKE %:pname% order by dt.id desc ")
    fun findDisciplineTypesByName(pageable: Pageable, @Param("pname") pname: String): Page<DisciplineType>

    fun findByName(@Param("name") name: String): DisciplineType?
}
