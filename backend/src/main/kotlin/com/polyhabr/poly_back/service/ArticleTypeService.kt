package com.polyhabr.poly_back.service

import com.polyhabr.poly_back.controller.model.articleType.request.ArticleTypeRequest
import com.polyhabr.poly_back.dto.ArticleTypeDto
import org.springframework.data.domain.Page

interface ArticleTypeService {
    fun getAll(offset: Int, size: Int): Page<ArticleTypeDto>

    fun getById(id: Long): ArticleTypeDto

    fun searchByName(prefix: String?, offset: Int, size: Int): Page<ArticleTypeDto>

    fun create(articleTypeRequest: ArticleTypeRequest): Long?

    fun update(id: Long, articleTypeRequest: ArticleTypeRequest): Pair<Boolean, String>

    fun delete(id: Long): Pair<Boolean, String>

}