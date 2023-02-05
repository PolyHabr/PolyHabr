package com.polyhabr.poly_back.service.impl

import com.polyhabr.poly_back.dto.ArticleDto
import com.polyhabr.poly_back.entity.Article
import com.polyhabr.poly_back.repository.ArticleRepository
import com.polyhabr.poly_back.service.ArticleService
import org.springframework.stereotype.Service

@Service
class ArticleServiceImpl(
    private val articleRepository: ArticleRepository,
) : ArticleService {
    override fun getAll(): List<ArticleDto> {
        return articleRepository.findAll().map{
            it.toDto()
        }
    }

    private fun Article.toDto(): ArticleDto =
        ArticleDto(
            id = this.id,
            date = this.date,
            filePdf = this.filePdf,
            likes = this.likes,
            previewText = this.previewText,
            typeId = this.type,
            userId = this.user,
        )
}