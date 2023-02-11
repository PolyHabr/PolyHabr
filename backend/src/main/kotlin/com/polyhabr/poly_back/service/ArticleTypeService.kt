package com.polyhabr.poly_back.service

import com.polyhabr.poly_back.dto.ArticleTypeDto

interface ArticleTypeService {
    fun getAll(): List<ArticleTypeDto>

}