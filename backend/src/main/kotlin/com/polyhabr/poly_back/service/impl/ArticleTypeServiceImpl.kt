package com.polyhabr.poly_back.service.impl

import com.polyhabr.poly_back.dto.ArticleTypeDto
import com.polyhabr.poly_back.service.ArticleTypeService
import org.springframework.stereotype.Service

@Service
class ArticleTypeServiceImpl : ArticleTypeService {
    override fun getAll(): List<ArticleTypeDto> {
        return listOf(
            ArticleTypeDto(1)
        )
    }
}