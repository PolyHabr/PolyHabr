package com.polyhabr.poly_back.service.impl

import com.polyhabr.poly_back.controller.model.article.request.ArticleRequest
import com.polyhabr.poly_back.controller.model.article.request.toDtoWithoutType
import com.polyhabr.poly_back.controller.utils.currentLogin
import com.polyhabr.poly_back.dto.ArticleDto
import com.polyhabr.poly_back.entity.Article
import com.polyhabr.poly_back.repository.ArticleRepository
import com.polyhabr.poly_back.repository.ArticleTypeRepository
import com.polyhabr.poly_back.repository.auth.UsersRepository
import com.polyhabr.poly_back.service.ArticleService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ArticleServiceImpl(
    private val articleRepository: ArticleRepository,
    private val usersRepository: UsersRepository,
    private val articleTypeRepository: ArticleTypeRepository
) : ArticleService {
    override fun getAll(
        offset: Int,
        size: Int,
    ): Page<ArticleDto> {
        return articleRepository
            .findAll(
                PageRequest.of(
                    offset,
                    size,
                )
            )
            .map { it.toDto() }
    }

    override fun getById(id: Long): ArticleDto {
        return articleRepository.findByIdOrNull(id)
            ?.toDto()
            ?: throw RuntimeException("Article not found")
    }

    override fun searchByName(prefix: String?, offset: Int, size: Int): Page<ArticleDto> =
        articleRepository
            .findArticleByName(
                PageRequest.of(
                    offset,
                    size,
                ), prefix ?: ""
            )
            .map { it.toDto() }

    override fun create(articleRequest: ArticleRequest): Long? {
        usersRepository.findByLogin(currentLogin())?.let { currentUser ->
            articleRequest.typeName?.let {
                articleTypeRepository.findByName(it)?.let { articleType ->
                    return articleRepository.save(
                        articleRequest
                            .toDtoWithoutType()
                            .apply {
                                typeId = articleType
                                userId = currentUser
                            }
                            .toEntity()
                    ).id
                } ?: throw RuntimeException("Article type not found")
            } ?: run {
                return articleRepository.save(
                    articleRequest
                        .toDtoWithoutType()
                        .apply {
                            userId = currentUser
                        }
                        .toEntity()
                ).id
            }
        } ?: throw RuntimeException("User not found")
    }

    override fun update(id: Long, articleRequest: ArticleRequest): Pair<Boolean, String> {
        articleRepository.findByIdOrNull(id)?.let { currentArticle ->
            currentArticle.apply {
                articleRequest.previewText?.let { previewText = it }
                articleRequest.filePdf?.let { filePdf = it }
                articleRequest.likes?.let { likes = it }
//                articleRequest.typeId?.let { typeId = it }
            }
            return articleRepository.save(currentArticle).id?.let { true to "Ok" } ?: (false to "Error while update")
        } ?: return false to "Article not found"
//        val existingAticle = articleRepository.findByIdOrNull(id)
//            ?: throw RuntimeException("Article not found")
//        existingAticle.previewText = articleRequest.previewText ?: throw RuntimeException("name not found")
//
//        return articleRepository.save(existingAticle).id?.let { true } ?: false
    }

    override fun delete(id: Long): Pair<Boolean, String> {
        return try {
            articleRepository.findByIdOrNull(id)?.let { curentArticle ->
                curentArticle.id?.let { id ->
                    articleRepository.deleteById(id)
                    true to "Article deleted"
                } ?: (false to "Article id not found")
            } ?: (false to "Article not found")
        } catch (e: Exception) {
            false to "Internal server error"
        }
//        val existingArticle = articleRepository.findByIdOrNull(id)
//            ?: throw RuntimeException("Article not found")
//        val existedId = existingArticle.id ?: throw RuntimeException("id not found")
//        articleRepository.deleteById(existedId)
    }

    private fun Article.toDto(): ArticleDto =
        ArticleDto(
            id = this.id,
            date = this.date,
            filePdf = this.filePdf,
            likes = this.likes,
            previewText = this.previewText,
            typeId = this.typeId,
            userId = this.userId,
        )

    private fun Article.toDtoWithoutType(): ArticleDto =
        ArticleDto(
            id = this.id,
            date = this.date,
            filePdf = this.filePdf,
            likes = this.likes,
            previewText = this.previewText,
        )

    private fun ArticleDto.toEntity() = Article(
        date = this.date!!,
        filePdf = this.filePdf!!,
        likes = this.likes!!,
        previewText = this.previewText!!,
        typeId = this.typeId!!,
        userId = this.userId!!,
    )


}