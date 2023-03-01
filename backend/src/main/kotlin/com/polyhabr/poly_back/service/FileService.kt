package com.polyhabr.poly_back.service

import com.polyhabr.poly_back.dto.FileCreatingDto
import com.polyhabr.poly_back.entity.File

interface FileService {
    fun create(dto: FileCreatingDto, fileName: String?, articleId: Long): File?
    fun findById(id: String): File?
    fun delete(fileId: String, articleId: Long)
}