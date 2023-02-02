package com.polyhabr.poly_back.service

import com.polyhabr.poly_back.dto.TagTypeDto

interface TagTypeService {
    fun getAll(): List<TagTypeDto>

}