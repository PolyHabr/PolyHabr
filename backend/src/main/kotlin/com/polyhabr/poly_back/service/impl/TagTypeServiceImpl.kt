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

    override fun update(id: Long, tagTypeRequest: TagTypeRequest): Pair<Boolean, String> {
        tagTypeRepository.findByIdOrNull(id)?.let { currentTagType ->
            currentTagType.apply {
                tagTypeRequest.name?.let { name = it }
            }
            return tagTypeRepository.save(currentTagType).id?.let { true to "Ok" } ?: (false to "Error while update")
        } ?: return false to "Tag type not found"
    }

    override fun delete(id: Long): Pair<Boolean, String> {
        return try {
            tagTypeRepository.findByIdOrNull(id)?.let { currentTagType ->
                currentTagType.id?.let { id ->
                    tagTypeRepository.deleteById(id)
                    true to "Tag type deleted"
                } ?: (false to "Tag type id not found")
            } ?: (false to "Tag type not found")
        } catch (e: Exception) {
            false to "Internal server error"
        }
    }

    private fun TagType.toDto(): TagTypeDto =
        TagTypeDto(
            id = this.id!!,
            name = this.name!!,
        )

    private fun TagTypeDto.toEntity() = TagType(
        name = this.name,
    )
}