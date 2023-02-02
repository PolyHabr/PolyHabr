package com.polyhabr.poly_back.repository

import com.polyhabr.poly_back.entity.TagType
import org.springframework.data.jpa.repository.JpaRepository

interface TagTypeRepository: JpaRepository<TagType, Int> {
}