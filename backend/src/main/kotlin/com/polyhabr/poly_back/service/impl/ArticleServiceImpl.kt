package com.polyhabr.poly_back.service.impl

import com.polyhabr.poly_back.controller.model.article.request.ArticleRequest
import com.polyhabr.poly_back.controller.model.article.request.toDto
import com.polyhabr.poly_back.dto.ArticleDto
import com.polyhabr.poly_back.entity.Article
import com.polyhabr.poly_back.repository.ArticleRepository
import com.polyhabr.poly_back.service.ArticleService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ArticleServiceImpl(
    private val articleRepository: ArticleRepository,
) : ArticleService {
    override fun getAll(
        offset: Int,
        size: Int,
    ): Page<ArticleDto> {
        return articleRepository
            .findAll(
                PageRequest.of(
                    offset,
                    size,
                )
            )
            .map { it.toDto() }
    }

    override fun getById(id: Long): ArticleDto {
        return articleRepository.findByIdOrNull(id)
            ?.toDto()
            ?: throw RuntimeException("Article not found")
    }

    override fun searchByName(prefix: String?, offset: Int, size: Int): Page<ArticleDto> =
        articleRepository
            .findArticleByName(
                PageRequest.of(
                    offset,
                    size,
                ), prefix ?: ""
            )
            .map { it.toDto() }

    override fun create(articleRequest: ArticleRequest): Long? {
        return articleRepository.save(
            articleRequest
                .toDto()
                .toEntity()
        ).id
    }

    override fun update(id: Long, articleRequest: ArticleRequest): Boolean {
        val existingAticle = articleRepository.findByIdOrNull(id)
            ?: throw RuntimeException("Article not found")
        existingAticle.previewText = articleRequest.previewText ?: throw RuntimeException("name not found")

        return articleRepository.save(existingAticle).id?.let { true } ?: false
    }

    override fun delete(id: Long) {
        val existingArticle = articleRepository.findByIdOrNull(id)
            ?: throw RuntimeException("Article not found")
        val existedId = existingArticle.id ?: throw RuntimeException("id not found")
        articleRepository.deleteById(existedId)
    }

    private fun Article.toDto(): ArticleDto =
        ArticleDto(
            id = this.id,
            date = this.date,
            filePdf = this.filePdf,
            likes = this.likes,
            previewText = this.previewText,
            typeId = this.typeId,
            userId = this.userId,
        )

    private fun ArticleDto.toEntity() = Article(
        date = this.date!!,
        filePdf = this.filePdf!!,
        likes = this.likes!!,
        previewText = this.previewText!!,
        typeId = this.typeId!!,
        userId = this.userId!!,
    )
}