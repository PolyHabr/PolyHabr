package com.polyhabr.poly_back.service

import com.polyhabr.poly_back.controller.model.tagType.request.TagTypeRequest
import com.polyhabr.poly_back.dto.TagTypeDto
import org.springframework.data.domain.Page

interface TagTypeService {
    fun getAll(offset: Int, size: Int): Page<TagTypeDto>

    fun getById(id: Long): TagTypeDto

    fun searchByName(prefix: String?, offset: Int, size: Int): Page<TagTypeDto>

    fun create(tagTypeRequest: TagTypeRequest): Long?

    fun update(id: Long, tagTypeRequest: TagTypeRequest): Boolean

    fun delete(id: Long)

}