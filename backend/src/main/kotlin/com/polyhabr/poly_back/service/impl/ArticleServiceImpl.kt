package com.polyhabr.poly_back.service.impl

import com.polyhabr.poly_back.controller.model.article.request.SortArticleRequest
import com.polyhabr.poly_back.controller.utils.currentLogin
import com.polyhabr.poly_back.dto.ArticleDto
import com.polyhabr.poly_back.dto.ArticleUpdateDto
import com.polyhabr.poly_back.entity.*
import com.polyhabr.poly_back.repository.*
import com.polyhabr.poly_back.repository.auth.UsersRepository
import com.polyhabr.poly_back.service.ArticleService
import com.polyhabr.poly_back.service.FileService
import org.joda.time.DateTime
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
    private val userToLikedArticleRepository: UserToLikedArticleRepository,
    private val disciplineTypeRepository: DisciplineTypeRepository,
    private val tagTypeRepository: TagTypeRepository,
    private val articleToTagTypeRepository: ArticleToTagTypeRepository,
    private val articleToDisciplineTypeRepository: ArticleToDisciplineTypeRepository,
    private val fileService: FileService,
    private val articleToFavRepository: ArticleToFavRepository
) : ArticleService {

    @Autowired
    lateinit var transactionTemplate: TransactionTemplate
    override fun getAll(
        offset: Int,
        size: Int,
        sorting: SortArticleRequest?,
    ): Page<ArticleDto> {
        val diff = sorting?.getMillis()
        val pageRequest = PageRequest.of(offset, size)
        val articles = if (diff == null) {
            articleRepository.findArticlesOrderDate(pageRequest)
        } else if (sorting.fieldView == true) {
            articleRepository.findArticlesWithLimitTimelineOrderView(
                pageRequest,
                nowmillis = DateTime.now().millis,
                diffmillis = diff
            )
        } else if (sorting.fieldRating == true) {
            articleRepository.findArticlesWithLimitTimelineOrderLike(
                pageRequest,
                nowmillis = DateTime.now().millis,
                diffmillis = diff
            )
        } else {
            articleRepository.findArticlesOrderDate(pageRequest)
        }
        return articles.map {
            val listDisciplineToSave = mutableListOf<String>()
            val listTagToSave = mutableListOf<String>()
            articleToDisciplineTypeRepository.findByArticle(it).forEach { articleToDisciplineType ->
                listDisciplineToSave.add(articleToDisciplineType.disciplineType?.name!!)
            }
            articleToTagTypeRepository.findByArticle(it).forEach { articleToTagType ->
                listTagToSave.add(articleToTagType.tagType?.name!!)
            }
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
                articleRepository.save(article.apply { this.view++ })
                return@execute article.toDto(
                    disciplineList = listDisciplineToSave,
                    tagList = listTagToSave
                )
            } ?: throw RuntimeException("Article not found")
        } ?: throw RuntimeException("SQL error")
    }

    override fun searchByName(prefix: String?, offset: Int, size: Int, sorting: SortArticleRequest?): Page<ArticleDto> {
        val searchText = prefix ?: ""
        val diff = sorting?.getMillis()
        val pageRequest = PageRequest.of(offset, size)
        val articles = if (diff == null) {
            articleRepository.searchByTitleArticlesOrderDate(pageable = pageRequest, titlesearch = searchText)
        } else if (sorting.fieldView == true) {
            articleRepository.searchByTitleArticlesWithLimitTimelineOrderView(
                pageable = pageRequest,
                nowmillis = DateTime.now().millis,
                diffmillis = diff,
                titlesearch = searchText
            )
        } else if (sorting.fieldRating == true) {
            articleRepository.searchByTitleArticlesWithLimitTimelineOrderLike(
                pageable = pageRequest,
                nowmillis = DateTime.now().millis,
                diffmillis = diff,
                titlesearch = searchText
            )
        } else {
            articleRepository.searchByTitleArticlesOrderDate(pageable = pageRequest, titlesearch = searchText)
        }
        return articles.map {
            val listDisciplineToSave = mutableListOf<String>()
            val listTagToSave = mutableListOf<String>()
            articleToDisciplineTypeRepository.findByArticle(it).forEach { articleToDisciplineType ->
                listDisciplineToSave.add(articleToDisciplineType.disciplineType?.name!!)
            }
            articleToTagTypeRepository.findByArticle(it).forEach { articleToTagType ->
                listTagToSave.add(articleToTagType.tagType?.name!!)
            }
            it.toDto(
                disciplineList = listDisciplineToSave,
                tagList = listTagToSave
            )
        }
    }

    override fun getByUserId(id: Long, offset: Int, size: Int): Page<ArticleDto> {
        val articles = articleRepository
            .findArticleByUserId(
                PageRequest.of(
                    offset,
                    size,
                ),
                id
            )
        return articles.map {
            val listDisciplineToSave = mutableListOf<String>()
            val listTagToSave = mutableListOf<String>()
            articleToDisciplineTypeRepository.findByArticle(it).forEach { articleToDisciplineType ->
                listDisciplineToSave.add(articleToDisciplineType.disciplineType?.name!!)
            }
            articleToTagTypeRepository.findByArticle(it).forEach { articleToTagType ->
                listTagToSave.add(articleToTagType.tagType?.name!!)
            }
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
                            ?: run {
                                return@run tagTypeRepository.save(
                                    TagType(name)
                                )
                            }
                        listTagToSave.add(tagType)
                    }

                    return transactionTemplate.execute {
//                        val savedFile = articleDto.fileDto?.let { file ->
//                            fileService.create(file, file.originalName, articleId)
//                                ?: throw RuntimeException("Error while create article")
//                        }
                        val article = articleRepository.save(
                            articleDto
                                .apply {
                                    typeId = articleType
                                    userId = currentUser
                                }
                                .toEntity()
//                                .apply {
//                                    file_id = savedFile
//                                }
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
                    articleUpdateDto.likes?.let { likes = it }
                    articleUpdateDto.title?.let { title = it }
                    articleUpdateDto.text?.let { text = it }
                    this.typeId = newArticleType
                }
                return articleRepository.save(currentArticle).id?.let { true to "Ok" }
                    ?: (false to "Error while update")
            } ?: return false to "Article not found"
        } ?: return false to "User not found"
    }

    override fun delete(id: Long): Pair<Boolean, String> {
        return try {
            usersRepository.findByLogin(currentLogin())?.let { currentUser ->
                articleRepository.findByIdOrNull(id)?.let { currentArticle ->
                    currentArticle.id?.let { id ->
                        if (currentUser.id != currentArticle.userId!!.id) {
                            return false to "You can't delete this article"
                        }
                        articleRepository.deleteById(id)
                        true to "Article deleted"
                    } ?: (false to "Article id not found")
                } ?: (false to "Article not found")
            } ?: (false to "User not found")
        } catch (e: Exception) {
            false to "Internal server error"
        }
    }

    override fun updateLikes(id: Long, isPlus: Boolean) {
        transactionTemplate.execute {
            usersRepository.findByLogin(currentLogin())?.let { currentUser ->
                if (isPlus) {
                    if (userToLikedArticleRepository.findArticleWithLike(id, currentUser.id) != null) {
                        throw Exception("Impossible")
                    } else {
                        articleRepository.addLikeByArticleId(id)
                        articleRepository.findByIdOrNull(id)?.let { article ->
                            userToLikedArticleRepository.save(
                                UserToLikedArticle(
                                    user = currentUser,
                                    article = article
                                )
                            )
                        } ?: throw Exception("Impossible")
                    }
                } else {
                    if (userToLikedArticleRepository.findArticleWithLike(id, currentUser.id) == null) {
                        throw Exception("Impossible")
                    } else {
                        articleRepository.decreaseLikeByArticleId(id)
                        userToLikedArticleRepository.findArticleWithLike(id, currentUser.id)?.let {
                            userToLikedArticleRepository.deleteById(it)
                        } ?: throw Exception("Impossible")
                    }
                }

            } ?: throw Exception("Go log in man")
        } ?: throw Exception("Internal exception")
    }

    override fun getFavArticle(offset: Int, size: Int): Page<ArticleDto> {
        usersRepository.findByLogin(currentLogin())?.let { currentUser ->
            val pageRequest = PageRequest.of(offset, size)
            val articles = articleToFavRepository.findByUserId(pageable = pageRequest, id = currentUser.id!!)
            return articles.map {
                val listDisciplineToSave = mutableListOf<String>()
                val listTagToSave = mutableListOf<String>()
                articleToDisciplineTypeRepository.findByArticle(it.articleId!!).forEach { articleToDisciplineType ->
                    listDisciplineToSave.add(articleToDisciplineType.disciplineType?.name!!)
                }
                articleToTagTypeRepository.findByArticle(it.articleId!!).forEach { articleToTagType ->
                    listTagToSave.add(articleToTagType.tagType?.name!!)
                }
                it?.articleId?.toDto(
                    disciplineList = listDisciplineToSave,
                    tagList = listTagToSave
                ) ?: throw Exception("Internal server error, fav article dont find")
            }
        } ?: throw Exception("You You are not logged in")
    }

    override fun updateFavArticle(idArticle: Long, goAddToFav: Boolean) {
        transactionTemplate.execute {
            usersRepository.findByLogin(currentLogin())?.let { currentUser ->
                articleToFavRepository.byArticleId(idArticle)?.let { articleToFav ->
                    if (goAddToFav) {
                        throw Exception("already added")
                    }
                    articleRepository.findById(idArticle).takeIf { it.isPresent }?.let {
                        val currentArticle = it.get()
                        articleToFavRepository.deleteById(articleToFav.id!!)
                        articleRepository.save(
                            currentArticle.apply {
                                isFav = false
                            }
                        )
                    } ?: throw throw Exception("Article not founded")
                } ?: run {
                    if (!goAddToFav) {
                        throw Exception("already deleted")
                    }
                    articleRepository.findById(idArticle).takeIf { it.isPresent }?.let {
                        val currentArticle = it.get()
                        articleToFavRepository.save(
                            ArticleToFav(
                                articleId = currentArticle,
                                userId = currentUser
                            )
                        )
                        articleRepository.save(
                            currentArticle.apply {
                                isFav = true
                            }
                        )
                    } ?: throw throw Exception("Article not founded")
                }
            } ?: throw Exception("You You are not logged in")
        } ?: throw Exception("Internal error")
    }

    private fun Article.toDto(
        disciplineList: List<String>,
        tagList: List<String>,
    ): ArticleDto =
        ArticleDto(
            id = this.id,
            date = DateTime(this.date),
            likes = this.likes,
            previewText = this.previewText,
            typeId = this.typeId,
            userId = this.userId,
            title = this.title,
            listDisciplineName = disciplineList,
            listTag = tagList,
            text = this.text,
            fileId = this.file_id?.id,
            viewCount = this.view,
            isSaveToFavourite = isFav,
            pdfId = file_id?.id
        )

    private fun ArticleDto.toEntity() = Article(
        date = this.date.millis,
        likes = this.likes,
        previewText = this.previewText,
        typeId = this.typeId!!,
        userId = this.userId!!,
        title = this.title,
        text = this.text,
        view = this.viewCount
    )
}