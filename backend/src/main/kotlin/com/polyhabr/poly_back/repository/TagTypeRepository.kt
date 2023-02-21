package com.polyhabr.poly_back.repository

import com.polyhabr.poly_back.entity.TagType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface TagTypeRepository: JpaRepository<TagType, Long>{
    @Query("SELECT tt FROM TagType tt WHERE tt.name LIKE %:pname%")
    fun findTagTypesByName(pageable: Pageable, @Param("pname") pname: String): Page<TagType>

}
