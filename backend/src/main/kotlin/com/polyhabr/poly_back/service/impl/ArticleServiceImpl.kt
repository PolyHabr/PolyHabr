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
import javax.transaction.Transactional


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
    private val articleToFavRepository: ArticleToFavRepository,
) : ArticleService {

    @Autowired
    lateinit var transactionTemplate: TransactionTemplate
    override fun getAll(
        offset: Int,
        size: Int,
        sorting: SortArticleRequest?,
    ): Page<ArticleDto> {
        val diff = sorting?.getMillis() ?: Long.MAX_VALUE
        val pageRequest = PageRequest.of(offset, size)
        val articles =
            sorting?.fieldView?.let {
                if (it) {
                    articleRepository.findArticlesWithLimitTimelineOrderViewASC(
                        pageRequest,
                        nowmillis = DateTime.now().millis,
                        diffmillis = diff
                    )
                } else {
                    articleRepository.findArticlesWithLimitTimelineOrderView(
                        pageRequest,
                        nowmillis = DateTime.now().millis,
                        diffmillis = diff
                    )
                }
            } ?: run {
                sorting?.fieldRating?.let {
                    if (it) {
                        articleRepository.findArticlesWithLimitTimelineOrderLikeASC(
                            pageRequest,
                            nowmillis = DateTime.now().millis,
                            diffmillis = diff
                        )
                    } else {
                        articleRepository.findArticlesWithLimitTimelineOrderLike(
                            pageRequest,
                            nowmillis = DateTime.now().millis,
                            diffmillis = diff
                        )
                    }
                }
            } ?: run {
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
            val isLiked = usersRepository.findByLogin(currentLogin())?.let { currentUser ->
                return@let userToLikedArticleRepository.findArticleWithLike(it.id!!, currentUser.id) != null
            } ?: false
            it.toDto(
                disciplineList = listDisciplineToSave,
                tagList = listTagToSave,
                isLiked = isLiked
            )
        }
    }

    @Transactional
    override fun getById(id: Long) =
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
            val isLiked = usersRepository.findByLogin(currentLogin())?.let { currentUser ->
                userToLikedArticleRepository.findArticleWithLike(article.id!!, currentUser.id) != null
            } ?: false
            val dto = article.toDto(
                disciplineList = listDisciplineToSave,
                tagList = listTagToSave,
                isLiked
            )
            return@let true to dto
        } ?: (false to null)

    override fun searchByName(
        prefix: String?,
        offset: Int,
        size: Int,
        sorting: SortArticleRequest?
    ): Page<ArticleDto> {
        val searchText = prefix ?: ""
        val diff = sorting?.getMillis() ?: Long.MAX_VALUE
        val pageRequest = PageRequest.of(offset, size)
        val articles =
            sorting?.fieldView?.let {
                if (it) {
                    articleRepository.searchByTitleArticlesWithLimitTimelineOrderViewASC(
                        pageable = pageRequest,
                        nowmillis = DateTime.now().millis,
                        diffmillis = diff,
                        titlesearch = searchText
                    )
                } else {
                    articleRepository.searchByTitleArticlesWithLimitTimelineOrderView(
                        pageable = pageRequest,
                        nowmillis = DateTime.now().millis,
                        diffmillis = diff,
                        titlesearch = searchText
                    )
                }
            } ?: run {
                sorting?.fieldRating?.let {
                    if (it) {
                        articleRepository.searchByTitleArticlesWithLimitTimelineOrderLikeASC(
                            pageable = pageRequest,
                            nowmillis = DateTime.now().millis,
                            diffmillis = diff,
                            titlesearch = searchText
                        )
                    } else {
                        articleRepository.searchByTitleArticlesWithLimitTimelineOrderLike(
                            pageable = pageRequest,
                            nowmillis = DateTime.now().millis,
                            diffmillis = diff,
                            titlesearch = searchText
                        )
                    }
                }
            } ?: run {
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
            val isLiked = usersRepository.findByLogin(currentLogin())?.let { currentUser ->
                return@let userToLikedArticleRepository.findArticleWithLike(it.id!!, currentUser.id) != null
            } ?: false
            it.toDto(
                disciplineList = listDisciplineToSave,
                tagList = listTagToSave,
                isLiked = isLiked
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
            val isLiked = usersRepository.findByLogin(currentLogin())?.let { currentUser ->
                return@let userToLikedArticleRepository.findArticleWithLike(it.id!!, currentUser.id) != null
            } ?: false
            it.toDto(
                disciplineList = listDisciplineToSave,
                tagList = listTagToSave,
                isLiked = isLiked
            )
        }
    }

    override fun create(articleDto: ArticleDto): Pair<Boolean, Long?> {
        usersRepository.findByLogin(currentLogin())?.let { currentUser ->
            return transactionTemplate.execute {
                return@execute articleDto.typeName?.let {
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
                        true to article.id
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
            return transactionTemplate.execute {
                val pageRequest = PageRequest.of(offset, size)
                val articles = articleToFavRepository.findByUserId(pageable = pageRequest, id = currentUser.id!!)
                return@execute articles.map {
                    val listDisciplineToSave = mutableListOf<String>()
                    val listTagToSave = mutableListOf<String>()
                    articleToDisciplineTypeRepository.findByArticle(it.articleId!!)
                        .forEach { articleToDisciplineType ->
                            listDisciplineToSave.add(articleToDisciplineType.disciplineType?.name!!)
                        }
                    articleToTagTypeRepository.findByArticle(it.articleId!!).forEach { articleToTagType ->
                        listTagToSave.add(articleToTagType.tagType?.name!!)
                    }
                    val isLiked = usersRepository.findByLogin(currentLogin())?.let { currentUser ->
                        userToLikedArticleRepository.findArticleWithLike(it.id!!, currentUser.id) != null
                    } ?: false
                    it?.articleId?.toDto(
                        disciplineList = listDisciplineToSave,
                        tagList = listTagToSave,
                        isLiked = isLiked
                    ) ?: throw Exception("Internal server error, fav article dont find")
                }
            } ?: throw Exception("Internal server error, fav article dont find")
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

    fun ArticleDto.toEntity() = Article(
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