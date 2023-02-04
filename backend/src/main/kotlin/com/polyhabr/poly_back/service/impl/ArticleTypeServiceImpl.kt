package com.polyhabr.poly_back.service.impl

import com.polyhabr.poly_back.dto.ArticleTypeDto
import com.polyhabr.poly_back.entity.ArticleType
import com.polyhabr.poly_back.repository.ArticleTypeRepository
import com.polyhabr.poly_back.service.ArticleTypeService
import org.springframework.stereotype.Service

@Service
class ArticleTypeServiceImpl(
    private val articleTypeRepository: ArticleTypeRepository
) : ArticleTypeService {
    override fun getAll(): List<ArticleTypeDto> {
        return articleTypeRepository.findAll().map{
            it.toDto()
        }
    }

    private fun ArticleType.toDto(): ArticleTypeDto =
        ArticleTypeDto(
            id = this.id,
            name = this.name,
        )
}