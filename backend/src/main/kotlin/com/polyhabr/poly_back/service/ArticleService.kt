package com.polyhabr.poly_back.service

import com.polyhabr.poly_back.controller.model.article.request.SortArticleRequest
import com.polyhabr.poly_back.dto.ArticleDto
import com.polyhabr.poly_back.dto.ArticleUpdateDto
import org.springframework.data.domain.Page

interface ArticleService {
    fun getAll(offset: Int, size: Int, sorting: SortArticleRequest?): Page<ArticleDto>

    fun getById(id: Long): ArticleDto

    fun searchByName(prefix: String?, offset: Int, size: Int, sorting: SortArticleRequest?): Page<ArticleDto>

    fun getByUserId(id: Long, offset: Int, size: Int): Page<ArticleDto>

    fun create(articleDto: ArticleDto): Pair<Boolean, Long?>

    fun update(id: Long, articleUpdateDto: ArticleUpdateDto): Pair<Boolean, String>

    fun delete(id: Long): Pair<Boolean, String>

    fun updateLikes(id: Long, isPlus: Boolean)

}