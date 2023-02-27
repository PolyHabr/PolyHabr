package com.polyhabr.poly_back.service

import com.polyhabr.poly_back.controller.model.file.FileOnlyRequest
import com.polyhabr.poly_back.controller.model.file.FileRequest
import com.polyhabr.poly_back.dto.FileCreatingDto
import com.polyhabr.poly_back.entity.File

interface FileService {
    fun create(dto: FileCreatingDto, fileName: String?): File?
    fun findById(id: String): File?
    fun delete(id: String)
}