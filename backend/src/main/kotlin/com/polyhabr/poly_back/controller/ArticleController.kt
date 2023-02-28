package com.polyhabr.poly_back.controller

import com.polyhabr.poly_back.controller.model.article.request.*
import com.polyhabr.poly_back.controller.model.article.response.ArticleListResponse
import com.polyhabr.poly_back.controller.model.article.response.*
import com.polyhabr.poly_back.controller.model.userToLikedArticle.request.UserToLikedArticleRequest
import com.polyhabr.poly_back.controller.model.userToLikedArticle.request.toDto
import com.polyhabr.poly_back.controller.model.userToLikedArticle.response.UserToLikedArticleUpdateResponse
import com.polyhabr.poly_back.service.ArticleService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.security.Principal
import javax.validation.ConstraintViolationException
import javax.validation.Valid
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

@RestController
@Validated
@RequestMapping("/articles")
class ArticleController(
    private val articleService: ArticleService,
) {
    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleConstraintViolationException(e: ConstraintViolationException): ResponseEntity<String?>? {
        return ResponseEntity("not valid due to validation error: " + e.message, HttpStatus.BAD_REQUEST)
    }

    @Operation(summary = "Article list")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Article list", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ArticleListResponse::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @GetMapping
    fun getAll(
        @Schema(example = "0") @PositiveOrZero @RequestParam("offset") offset: Int,
        @Schema(example = "1") @Positive @RequestParam("size") size: Int,

        ): ResponseEntity<ArticleListResponse> {
        val rawResponse = articleService
            .getAll(
                offset = offset,
                size = size,
            )
        return ResponseEntity.ok(rawResponse.toListResponse())
    }

    @Operation(summary = "Article find by id")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Article", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ArticleResponse::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @GetMapping("/byId")
    fun getById(
        @Positive @RequestParam("id") id: Long
    ): ResponseEntity<ArticleResponse> {
        val response = articleService
            .getById(id)
            .toResponse()
        return response.let {
            ResponseEntity.ok(response)
        }
    }

    @Operation(summary = "Search articles by prefix")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Article", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ArticleListResponse::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @GetMapping("/search")
    fun searchArticlesByName(
        @Schema(example = "physics") @RequestParam("prefix") prefix: String?,
        @Schema(example = "0") @PositiveOrZero @RequestParam("offset") offset: Int,
        @Schema(example = "1") @Positive @RequestParam("size") size: Int,
    ): ResponseEntity<ArticleListResponse> {
        val rawResponse = articleService
            .searchByName(prefix, offset, size)
        return ResponseEntity.ok(rawResponse.toListResponse())
    }

    @Operation(summary = "Article create")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "ArticleCreateResponse", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ArticleCreateResponse::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @PostMapping(path = ["/create"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    fun create(
        @RequestPart(name = "model") @Valid articleRequest: ArticleRequest,
        @RequestPart(name = "file") @Schema(description = "nullable, pfd byte") file: MultipartFile? = null
    ): ResponseEntity<ArticleCreateResponse> {
        val (success, id) = articleService.create(articleRequest.toDtoWithoutType(file?.bytes, file?.originalFilename))
        val response = ArticleCreateResponse(id = id, isSuccess = success)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "Update article by id")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "ArticleUpdateResponse", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ArticleUpdateResponse::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @PutMapping("/update")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    fun update(
        @Positive @RequestParam("id") id: Long,
        @RequestBody articleUpdateRequest: ArticleUpdateRequest
    ): ResponseEntity<ArticleUpdateResponse> {
        val (success, message) = articleService.update(id, articleUpdateRequest.toDto())
        val response = ArticleUpdateResponse(success, message)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "Delete article by id")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "ArticleUpdateResponse", content = [Content()]),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    fun delete(
        @Positive @RequestParam(value = "id") id: Long
    ): ResponseEntity<String> {
        val (success, message) = articleService.delete(id)
        return if (success) {
            ResponseEntity.ok().body(message)
        } else {
            ResponseEntity.internalServerError().body(message)
        }
    }

    @Operation(summary = "Add like in article by id")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "ArticleUpdateResponse", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ArticleUpdateResponse::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @PostMapping("/add_like")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    fun addLike(
        @Positive @RequestParam("articleId") id: Long,
    ): ResponseEntity<Unit> {
        articleService.updateLikes(id = id, isPlus = true)
        return ResponseEntity.ok().build()
    }

    @Operation(summary = "Decrease like in article by id")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "ArticleUpdateResponse", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ArticleUpdateResponse::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @PostMapping("/decrease_like")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    fun decreaseLike(
        @Positive @RequestParam("articleId") id: Long,
    ): ResponseEntity<Unit> {
        articleService.updateLikes(id = id, isPlus = false)
        return ResponseEntity.ok().build()
    }
}