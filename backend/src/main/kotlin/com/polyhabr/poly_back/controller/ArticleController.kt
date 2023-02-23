package com.polyhabr.poly_back.controller

import com.polyhabr.poly_back.controller.model.article.response.ArticleListResponse
import com.polyhabr.poly_back.controller.model.article.response.*
import com.polyhabr.poly_back.controller.model.article.request.ArticleRequest
import com.polyhabr.poly_back.service.ArticleService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
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
    ): ResponseEntity<ArticleListResponse>{
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
        return ResponseEntity.ok(response)
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
    @PostMapping("/create")
    fun create(
        @Valid @RequestBody articleRequest: ArticleRequest
    ): ResponseEntity<ArticleCreateResponse> {
        val id = articleService.create(articleRequest)
        val success = id != null
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
    fun update(
        @Positive @RequestParam("id") id: Long,
        @Valid @RequestBody articleRequest: ArticleRequest
    ): ResponseEntity<ArticleUpdateResponse> {
        val success = articleService.update(id, articleRequest)
        val response = ArticleUpdateResponse(success)
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
    fun delete(
        @Positive @RequestParam(value = "id") id: Long
    ): ResponseEntity<Unit> {
        return try {
            articleService.delete(id)
            ResponseEntity.ok().build()
        } catch (e: Exception) {
            ResponseEntity.badRequest().build()
        }
    }

}