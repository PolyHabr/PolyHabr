package com.polyhabr.poly_back.service.impl

import com.polyhabr.poly_back.controller.model.comment.request.CommentRequest
import com.polyhabr.poly_back.controller.model.comment.request.toDto
import com.polyhabr.poly_back.dto.CommentDto
import com.polyhabr.poly_back.entity.Comment
import com.polyhabr.poly_back.repository.CommentRepository
import com.polyhabr.poly_back.service.CommentService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CommentServiceImpl(
    private val commentRepository: CommentRepository
) : CommentService {
    override fun getAll(
        offset: Int,
        size: Int,
    ): Page<CommentDto> {
        return commentRepository
            .findAll(
                PageRequest.of(
                    offset,
                    size,
                )
            )
            .map { it.toDto() }
    }

    override fun getById(id: Long): CommentDto {
        return commentRepository.findByIdOrNull(id)
            ?.toDto()
            ?: throw RuntimeException("Comment not found")
    }

    override fun searchByName(prefix: String?, offset: Int, size: Int): Page<CommentDto> =
        commentRepository
            .findCommentsByName(
                PageRequest.of(
                    offset,
                    size,
                ), prefix ?: ""
            )
            .map { it.toDto() }


    override fun create(commentRequest: CommentRequest): Long? {
        return commentRepository.save(
            commentRequest
                .toDto()
                .toEntity()
        ).id
    }

    override fun update(id: Long, commentRequest: CommentRequest): Boolean {
        val existingComment = commentRepository.findByIdOrNull(id)
            ?: throw RuntimeException("User not found")
        existingComment.text = commentRequest.text ?: throw RuntimeException("text not found")

        return commentRepository.save(existingComment).id?.let { true } ?: false
    }

    override fun delete(id: Long) {
        val existingComment = commentRepository.findByIdOrNull(id)
            ?: throw RuntimeException("Comment not found")
        val existedId = existingComment.id ?: throw RuntimeException("id not found")
        commentRepository.deleteById(existedId)
    }

    private fun Comment.toDto(): CommentDto =
        CommentDto(
            id = this.id,
            text = this.text,
            articleId = this.articleId,
            userId = this.userId,
        )

    private fun CommentDto.toEntity() = Comment(
        text = this.text!!,
        articleId = this.articleId!!,
        userId = this.userId!!,
    )
}