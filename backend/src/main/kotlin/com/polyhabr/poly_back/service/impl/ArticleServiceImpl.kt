package com.polyhabr.poly_back.service.impl

import com.polyhabr.poly_back.controller.model.article.request.ArticleUpdateRequest
import com.polyhabr.poly_back.controller.utils.currentLogin
import com.polyhabr.poly_back.dto.ArticleDto
import com.polyhabr.poly_back.dto.ArticleUpdateDto
import com.polyhabr.poly_back.entity.*
import com.polyhabr.poly_back.repository.*
import com.polyhabr.poly_back.repository.auth.UsersRepository
import com.polyhabr.poly_back.service.ArticleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate


@Service
class ArticleServiceImpl(
    private val articleRepository: ArticleRepository,
    private val usersRepository: UsersRepository,
    private val articleTypeRepository: ArticleTypeRepository,
    private val disciplineTypeRepository: DisciplineTypeRepository,
    private val tagTypeRepository: TagTypeRepository,
    private val articleToTagTypeRepository: ArticleToTagTypeRepository,
    private val articleToDisciplineTypeRepository: ArticleToDisciplineTypeRepository,
) : ArticleService {

    @Autowired
    lateinit var transactionTemplate: TransactionTemplate
    override fun getAll(
        offset: Int,
        size: Int,
    ): Page<ArticleDto> {
        val articles = articleRepository
            .findAll(
                PageRequest.of(
                    offset,
                    size,
                )
            )
        val listDisciplineToSave = mutableListOf<String>()
        val listTagToSave = mutableListOf<String>()
        articles.content.forEach { article ->
            articleToDisciplineTypeRepository.findByArticle(article).forEach { articleToDisciplineType ->
                listDisciplineToSave.add(articleToDisciplineType.disciplineType?.name!!)
            }
            articleToTagTypeRepository.findByArticle(article).forEach { articleToTagType ->
                listTagToSave.add(articleToTagType.tagType?.name!!)
            }
        }

        return articles.map {
            it.toDto(
                disciplineList = listDisciplineToSave,
                tagList = listTagToSave
            )
        }
    }

    override fun getById(id: Long): ArticleDto {
        return transactionTemplate.execute {
            articleRepository.findByIdOrNull(id)?.let { article ->
                val listDisciplineToSave = mutableListOf<String>()
                val listTagToSave = mutableListOf<String>()
                articleToDisciplineTypeRepository.findByArticle(article).forEach { articleToDisciplineType ->
                    listDisciplineToSave.add(articleToDisciplineType.disciplineType?.name!!)
                }
                articleToTagTypeRepository.findByArticle(article).forEach { articleToTagType ->
                    listTagToSave.add(articleToTagType.tagType?.name!!)
                }
                return@execute article.toDto(
                    disciplineList = listDisciplineToSave,
                    tagList = listTagToSave
                )
            } ?: throw RuntimeException("Article not found")
        } ?: throw RuntimeException("SQL error")
    }

    override fun searchByName(prefix: String?, offset: Int, size: Int): Page<ArticleDto> {
        val articles = articleRepository
            .findArticleByName(
                PageRequest.of(
                    offset,
                    size,
                ), prefix ?: ""
            )
        val listDisciplineToSave = mutableListOf<String>()
        val listTagToSave = mutableListOf<String>()
        articles.content.forEach { article ->
            articleToDisciplineTypeRepository.findByArticle(article).forEach { articleToDisciplineType ->
                listDisciplineToSave.add(articleToDisciplineType.disciplineType?.name!!)
            }
            articleToTagTypeRepository.findByArticle(article).forEach { articleToTagType ->
                listTagToSave.add(articleToTagType.tagType?.name!!)
            }
        }
        return articles.map {
            it.toDto(
                disciplineList = listDisciplineToSave,
                tagList = listTagToSave
            )
        }
    }

    override fun create(articleDto: ArticleDto): Pair<Boolean, Long?> {
        usersRepository.findByLogin(currentLogin())?.let { currentUser ->
            articleDto.typeName?.let {
                articleTypeRepository.findByName(it)?.let { articleType ->
                    val listDisciplineToSave = mutableListOf<DisciplineType>()
                    articleDto.listDisciplineName.forEach { name ->
                        val disciplineType = disciplineTypeRepository.findByName(name)
                            ?: throw RuntimeException("Discipline by name not found")
                        listDisciplineToSave.add(disciplineType)
                    }

                    val listTagToSave = mutableListOf<TagType>()
                    articleDto.listTag.forEach { name ->
                        val tagType = tagTypeRepository.findByName(name)
                            ?: throw RuntimeException("Tag by name not found")
                        listTagToSave.add(tagType)
                    }

                    return transactionTemplate.execute {
                        val article = articleRepository.save(
                            articleDto
                                .apply {
                                    typeId = articleType
                                    userId = currentUser
                                }
                                .toEntity()
                        )
                        if (article.id == null) {
                            throw RuntimeException("Error while create article")
                        }
                        listDisciplineToSave.forEach { discipline ->
                            val obj = articleToDisciplineTypeRepository.save(
                                ArticleToDisciplineType(
                                    article = article,
                                    disciplineType = discipline
                                )
                            )
                            if (obj.id == null) {
                                throw RuntimeException("Error while create article")
                            }
                        }
                        listTagToSave.forEach { tag ->
                            val obj = articleToTagTypeRepository.save(
                                ArticleToTagType(
                                    article = article,
                                    tagType = tag
                                )
                            )
                            if (obj.id == null) {
                                throw RuntimeException("Error while create article")
                            }
                        }
                        return@execute true to article.id
                    } ?: (false to null)
                } ?: throw RuntimeException("Article type not found")
            } ?: throw RuntimeException("Article type not found")
        } ?: throw RuntimeException("User not found")
    }

    override fun update(id: Long, articleUpdateDto: ArticleUpdateDto): Pair<Boolean, String> {
        usersRepository.findByLogin(currentLogin())?.let { _ ->
            articleRepository.findByIdOrNull(id)?.let { currentArticle ->
                val newArticleType = articleUpdateDto.typeName?.let { articleTypeRepository.findByName(it) }
                currentArticle.apply {
                    articleUpdateDto.previewText?.let { previewText = it }
                    articleUpdateDto.filePdf?.let { filePdf = it }
                    articleUpdateDto.likes?.let { likes = it }
                    articleUpdateDto.title?.let { title = it }
                    this.typeId = newArticleType
                }
                return articleRepository.save(currentArticle).id?.let { true to "Ok" }
                    ?: (false to "Error while update")
            } ?: return false to "Article not found"
        } ?: return false to "User not found"
    }

    override fun delete(id: Long): Pair<Boolean, String> {
        return try {
            usersRepository.findByLogin(currentLogin())?.let { _ ->
                articleRepository.findByIdOrNull(id)?.let { currentArticle ->
                    currentArticle.id?.let { id ->
                        articleRepository.deleteById(id)
                        true to "Article deleted"
                    } ?: (false to "Article id not found")
                } ?: (false to "Article not found")
            } ?: (false to "User not found")
        } catch (e: Exception) {
            false to "Internal server error"
        }
    }

    private fun Article.toDto(
        disciplineList: List<String>,
        tagList: List<String>,
    ): ArticleDto =
        ArticleDto(
            id = this.id,
            date = this.date,
            filePdf = this.filePdf,
            likes = this.likes,
            previewText = this.previewText,
            typeId = this.typeId,
            userId = this.userId,
            title = this.title,
            listDisciplineName = disciplineList,
            listTag = tagList,
        )

    private fun ArticleDto.toEntity() = Article(
        date = this.date,
        filePdf = this.filePdf!!,
        likes = this.likes,
        previewText = this.previewText,
        typeId = this.typeId!!,
        userId = this.userId!!,
        title = this.title,
    )
}