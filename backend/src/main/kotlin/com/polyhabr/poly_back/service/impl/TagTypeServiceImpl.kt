package com.polyhabr.poly_back.service.impl

import com.polyhabr.poly_back.dto.TagTypeDto
import com.polyhabr.poly_back.entity.TagType
import com.polyhabr.poly_back.repository.TagTypeRepository
import com.polyhabr.poly_back.service.TagTypeService
import org.springframework.stereotype.Service

@Service
class TagTypeServiceImpl(
    private val tagTypeRepository: TagTypeRepository
) : TagTypeService {
    override fun getAll(): List<TagTypeDto> {
        return tagTypeRepository.findAll().map{
            it.toDto()
        }
    }
    private fun TagType.toDto(): TagTypeDto =
        TagTypeDto(
            id = this.id,
            name = this.name,
        )
}