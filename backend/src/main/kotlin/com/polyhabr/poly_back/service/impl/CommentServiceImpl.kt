package com.polyhabr.poly_back.service.impl

import com.polyhabr.poly_back.dto.CommentDto
import com.polyhabr.poly_back.entity.Comment
import com.polyhabr.poly_back.repository.CommentRepository
import com.polyhabr.poly_back.service.CommentService
import org.springframework.stereotype.Service

@Service
class CommentServiceImpl(
    private val commentRepository: CommentRepository
) : CommentService {
    override fun getAll(): List<CommentDto> {
        return commentRepository.findAll().map {
            it.toDto()
        }
    }

    private fun Comment.toDto(): CommentDto =
        CommentDto(
            id = this.id,
            text = this.text,
            articleId = this.article?.id,
            userId = this.user?.id,
        )
}