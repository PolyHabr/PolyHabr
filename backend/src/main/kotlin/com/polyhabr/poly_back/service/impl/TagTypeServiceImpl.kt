package com.polyhabr.poly_back.service.impl

import com.polyhabr.poly_back.controller.model.tagType.request.TagTypeRequest
import com.polyhabr.poly_back.controller.model.tagType.request.toDto
import com.polyhabr.poly_back.dto.TagTypeDto
import com.polyhabr.poly_back.entity.TagType
import com.polyhabr.poly_back.repository.TagTypeRepository
import com.polyhabr.poly_back.service.TagTypeService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TagTypeServiceImpl(
    private val tagTypeRepository: TagTypeRepository
) : TagTypeService {
    override fun getAll(offset: Int, size: Int): Page<TagTypeDto> {
        return tagTypeRepository
            .findAll(
                PageRequest.of(
                    offset,
                    size,
                )
            )
            .map { it.toDto() }
    }

    override fun getById(id: Long): TagTypeDto {
        return tagTypeRepository.findByIdOrNull(id)
            ?.toDto()
            ?: throw RuntimeException("Tag type not found")
    }

    override fun searchByName(prefix: String?, offset: Int, size: Int): Page<TagTypeDto> =
        tagTypeRepository
            .findTagTypesByName(
                PageRequest.of(
                    offset,
                    size,
                ), prefix ?: ""
            )
            .map { it.toDto() }

    override fun create(tagTypeRequest: TagTypeRequest): Long? {
        return tagTypeRepository.save(
            tagTypeRequest
                .toDto()
                .toEntity()
        ).id
    }

    override fun update(id: Long, tagTypeRequest: TagTypeRequest): Boolean {
        val existingTagType = tagTypeRepository.findByIdOrNull(id)
            ?: throw RuntimeException("Tag type not found")
        existingTagType.name = tagTypeRequest.name ?: throw RuntimeException("name not found")

        return tagTypeRepository.save(existingTagType).id?.let { true } ?: false
    }

    override fun delete(id: Long) {
        val existingTagType = tagTypeRepository.findByIdOrNull(id)
            ?: throw RuntimeException("Tag type not found")
        val existedId = existingTagType.id ?: throw RuntimeException("id not found")
        tagTypeRepository.deleteById(existedId)
    }

    private fun TagType.toDto(): TagTypeDto =
        TagTypeDto(
            id = this.id,
            name = this.name,
        )

    private fun TagTypeDto.toEntity() = TagType(
        name = this.name!!,
    )
}