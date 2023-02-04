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
            file_pdf = this.file_pdf,
            likes = this.likes,
            preview_text = this.preview_text,
            type_id = this.articlesType,
            user_id = this.users,
        )
}