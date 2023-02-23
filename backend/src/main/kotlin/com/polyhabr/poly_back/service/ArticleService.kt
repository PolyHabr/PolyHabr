package com.polyhabr.poly_back.service

import com.polyhabr.poly_back.controller.model.article.request.ArticleRequest
import com.polyhabr.poly_back.dto.ArticleDto
import org.springframework.data.domain.Page

interface ArticleService {
    fun getAll(offset: Int, size: Int): Page<ArticleDto>

    fun getById(id: Long): ArticleDto

    fun searchByName(prefix: String?, offset: Int, size: Int): Page<ArticleDto>

    fun create(articleRequest: ArticleRequest): Long?

    fun update(id: Long, articleRequest: ArticleRequest): Boolean

    fun delete(id: Long)

}