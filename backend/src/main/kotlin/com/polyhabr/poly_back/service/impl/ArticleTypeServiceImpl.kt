package com.polyhabr.poly_back.service.impl

import com.polyhabr.poly_back.controller.model.articleType.request.ArticleTypeRequest
import com.polyhabr.poly_back.controller.model.articleType.request.toDto
import com.polyhabr.poly_back.dto.ArticleTypeDto
import com.polyhabr.poly_back.entity.ArticleType
import com.polyhabr.poly_back.repository.ArticleTypeRepository
import com.polyhabr.poly_back.service.ArticleTypeService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ArticleTypeServiceImpl(
    private val articleTypeRepository: ArticleTypeRepository
) : ArticleTypeService {
    override fun getAll(offset: Int, size: Int): Page<ArticleTypeDto> {
        return articleTypeRepository
            .findAll(
                PageRequest.of(
                    offset,
                    size,
                )
            )
            .map { it.toDto() }
    }

    override fun getById(id: Long): ArticleTypeDto {
        return articleTypeRepository.findByIdOrNull(id)
            ?.toDto()
            ?: throw RuntimeException("Article type not found")
    }

    override fun searchByName(prefix: String?, offset: Int, size: Int): Page<ArticleTypeDto> =
        articleTypeRepository
            .findArticleTypesByName(
                PageRequest.of(
                    offset,
                    size,
                ), prefix ?: ""
            )
            .map { it.toDto() }

    override fun create(articleTypeRequest: ArticleTypeRequest): Long? {
        return articleTypeRepository.save(
            articleTypeRequest
                .toDto()
                .toEntity()
        ).id
    }

    override fun update(id: Long, articleTypeRequest: ArticleTypeRequest): Pair<Boolean, String> {
        articleTypeRepository.findByIdOrNull(id)?.let { currentArticleType ->
            currentArticleType.apply {
                articleTypeRequest.name?.let { name = it }
            }
            return articleTypeRepository.save(currentArticleType).id?.let { true to "Ok" } ?: (false to "Error while update")
        }?: return false to "Article type not found"

//        val existingArticleType = articleTypeRepository.findByIdOrNull(id)
//            ?: throw RuntimeException("Article type not found")
//        existingArticleType.name = articleTypeRequest.name ?: throw RuntimeException("name not found")
//
//        return articleTypeRepository.save(existingArticleType).id?.let { true } ?: false
    }

    override fun delete(id: Long): Pair<Boolean, String> {
        return try {
            articleTypeRepository.findByIdOrNull(id)?.let { currentArticleType ->
                currentArticleType.id?.let { id ->
                    articleTypeRepository.deleteById(id)
                    true to "Article type deleted"
                }?: (false to "Article type id not found")
            }?: (false to "Article type not found")
        }
        catch (e:Exception){
            false to "Internal server error"
        }



//        val existingArticleType = articleTypeRepository.findByIdOrNull(id)
//            ?: throw RuntimeException("Article type not found")
//        val existedId = existingArticleType.id ?: throw RuntimeException("id not found")
//        articleTypeRepository.deleteById(existedId)
    }

    private fun ArticleType.toDto(): ArticleTypeDto =
        ArticleTypeDto(
            id = this.id,
            name = this.name,
        )

    private fun ArticleTypeDto.toEntity() = ArticleType(
        name = this.name!!,
    )
}