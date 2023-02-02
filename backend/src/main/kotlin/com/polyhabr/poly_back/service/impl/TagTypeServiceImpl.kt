package com.polyhabr.poly_back.service.impl

import com.polyhabr.poly_back.dto.TagTypeDto
import com.polyhabr.poly_back.service.TagTypeService
import org.springframework.stereotype.Service

@Service
class TagTypeServiceImpl : TagTypeService {
    override fun getAll(): List<TagTypeDto> {
        return listOf(
            TagTypeDto(1)
        )
    }
}