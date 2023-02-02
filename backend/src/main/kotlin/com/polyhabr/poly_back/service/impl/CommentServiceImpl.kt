package com.polyhabr.poly_back.service.impl

import com.polyhabr.poly_back.dto.CommentDto
import com.polyhabr.poly_back.service.CommentService
import org.springframework.stereotype.Service

@Service
class CommentServiceImpl : CommentService {
    override fun getAll(): List<CommentDto> {
        return listOf(
            CommentDto(1)
        )
    }
}