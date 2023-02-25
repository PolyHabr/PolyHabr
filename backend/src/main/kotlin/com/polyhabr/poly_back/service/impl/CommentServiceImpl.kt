package com.polyhabr.poly_back.service.impl

import com.polyhabr.poly_back.controller.model.comment.request.CommentRequest
import com.polyhabr.poly_back.controller.model.comment.request.toDtoWithoutUser
import com.polyhabr.poly_back.controller.utils.currentLogin
import com.polyhabr.poly_back.dto.CommentDto
import com.polyhabr.poly_back.entity.Comment
import com.polyhabr.poly_back.repository.ArticleRepository
import com.polyhabr.poly_back.repository.CommentRepository
import com.polyhabr.poly_back.repository.auth.UsersRepository
import com.polyhabr.poly_back.service.CommentService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CommentServiceImpl(
    private val commentRepository: CommentRepository,
    private val usersRepository: UsersRepository,
    private val articleRepository: ArticleRepository
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
//        usersRepository.findByLogin(currentLogin())?.let { currentUser ->
//            commentRequest.articleId?.let {
//                articleRepository.findByIdOrNull(it)?.let { currentArticle ->
//                    return commentRepository.save(
//                        commentRequest
//                            .toDtoWithoutUser()
//                            .apply {
//                                articleId = currentArticle
//                                userId = currentUser
//                            }
//                            .toEntity()
//                    ).id
//                } ?: throw RuntimeException("Article not found")
//            } ?: run {
//                return commentRepository.save(
//                    commentRequest
//                        .toDtoWithoutUser()
//                        .apply {
//                            userId = currentUser
//                        }
//                        .toEntity()
//                ).id
//            }
//        }
        usersRepository.findByLogin(currentLogin())?.let { currentUser ->
            commentRequest.articleId?.let {
                articleRepository.findByIdOrNull(it)?.let { currentArticle ->
                    return commentRepository.save(
                        commentRequest
                            .toDtoWithoutUser()
                            .apply {
                                articleId = currentArticle
                                userId = currentUser
                            }
                            .toEntity()
                    ).id
                } ?: throw RuntimeException("Article type not found")
            } ?: run {
                return commentRepository.save(
                    commentRequest
                        .toDtoWithoutUser()
                        .apply {
                            userId = currentUser
                        }
                        .toEntity()
                ).id
            }
        } ?: throw RuntimeException("User not found")
    }

    override fun update(id: Long, commentRequest: CommentRequest): Pair<Boolean, String> {
        commentRepository.findByIdOrNull(id)?.let { currentCommentType ->
            currentCommentType.apply {
                commentRequest.text?.let { text = it }
            }
            return commentRepository.save(currentCommentType).id?.let { true to "Ok" }
                ?: (false to "Error while update")
        } ?: return false to "Comment not found"


//        val existingComment = commentRepository.findByIdOrNull(id)
//            ?: throw RuntimeException("User not found")
//        existingComment.text = commentRequest.text ?: throw RuntimeException("text not found")
//
//        return commentRepository.save(existingComment).id?.let { true } ?: false
    }

    override fun delete(id: Long): Pair<Boolean, String> {
        return try {
            commentRepository.findByIdOrNull(id)?.let { currentCommentType ->
                currentCommentType.id?.let { id ->
                    commentRepository.deleteById(id)
                    true to "Comment deleted"
                } ?: (false to "Comment id not found")
            } ?: (false to "Comment not found")
        } catch (e: Exception) {
            false to "Internal server error"
        }


//        val existingComment = commentRepository.findByIdOrNull(id)
//            ?: throw RuntimeException("Comment not found")
//        val existedId = existingComment.id ?: throw RuntimeException("id not found")
//        commentRepository.deleteById(existedId)
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