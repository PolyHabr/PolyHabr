package com.polyhabr.poly_back.service

import com.polyhabr.poly_back.controller.model.comment.request.CommentRequest
import com.polyhabr.poly_back.dto.CommentDto
import org.springframework.data.domain.Page

interface CommentService {
    fun getAll(offset: Int, size: Int): Page<CommentDto>

    fun getById(id: Long): CommentDto

    fun searchByName(prefix: String?, offset: Int, size: Int): Page<CommentDto>

    fun create(commentRequest: CommentRequest): Long?

    fun update(id: Long, commentRequest: CommentRequest): Boolean

    fun delete(id: Long)

}