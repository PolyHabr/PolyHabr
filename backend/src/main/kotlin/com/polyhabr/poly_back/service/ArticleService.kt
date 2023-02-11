package com.polyhabr.poly_back.service

import com.polyhabr.poly_back.dto.ArticleDto

interface ArticleService {
    fun getAll(): List<ArticleDto>
}