package com.polyhabr.poly_back.service

import com.polyhabr.poly_back.controller.model.comment.request.CommentRequest
import com.polyhabr.poly_back.dto.CommentDto
import org.springframework.data.domain.Page

interface CommentService {
    fun getAll(offset: Int, size: Int): Page<CommentDto>

    fun getByArticleIdAll(offset: Int, size: Int, articleId: Long): Page<CommentDto>

    fun getById(id: Long): CommentDto

    fun searchByName(prefix: String?, offset: Int, size: Int): Page<CommentDto>

    fun create(commentRequest: CommentRequest): CommentDto

    fun update(id: Long, commentRequest: CommentRequest): Pair<Boolean, String>

    fun delete(id: Long): Pair<Boolean, String>

}