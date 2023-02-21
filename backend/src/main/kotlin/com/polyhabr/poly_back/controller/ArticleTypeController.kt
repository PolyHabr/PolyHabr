package com.polyhabr.poly_back.controller

import com.polyhabr.poly_back.controller.model.articleType.request.ArticleTypeRequest
import com.polyhabr.poly_back.controller.model.articleType.response.*
import com.polyhabr.poly_back.service.ArticleTypeService
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
@RequestMapping("/article_types")
class ArticleTypeController(
    private val articleTypeService: ArticleTypeService,
) {
    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleConstraintViolationException(e: ConstraintViolationException): ResponseEntity<String?>? {
        return ResponseEntity("not valid due to validation error: " + e.message, HttpStatus.BAD_REQUEST)
    }

    @Operation(summary = "Article type list")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Article type list", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ArticleTypeListResponse::class)
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
    ): ResponseEntity<ArticleTypeListResponse> {
        val rawResponse = articleTypeService
            .getAll(
                offset = offset,
                size = size,
            )
        return ResponseEntity.ok(rawResponse.toListResponse())
    }

    @Operation(summary = "Article type find by id")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "ArticleType", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ArticleTypeResponse::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @GetMapping("/byId")
    fun getById(
        @Positive @RequestParam("id") id: Long
    ): ResponseEntity<ArticleTypeResponse> {
        val response = articleTypeService
            .getById(id)
            .toResponse()
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "Search article types by prefix")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "ArticleType", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ArticleTypeListResponse::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @GetMapping("/search")
    fun searchArticleTypesByName(
        @Schema(example = "physics") @RequestParam("prefix") prefix: String?,
        @Schema(example = "0") @PositiveOrZero @RequestParam("offset") offset: Int,
        @Schema(example = "1") @Positive @RequestParam("size") size: Int,
    ): ResponseEntity<ArticleTypeListResponse> {
        val rawResponse = articleTypeService
            .searchByName(prefix, offset, size)
        return ResponseEntity.ok(rawResponse.toListResponse())
    }

    @Operation(summary = "Article Type create")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "ArticleTypeCreateResponse", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ArticleTypeCreateResponse::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @PostMapping("/create")
    fun create(
        @Valid @RequestBody articleTypeRequest: ArticleTypeRequest
    ): ResponseEntity<ArticleTypeCreateResponse> {
        val id = articleTypeService.create(articleTypeRequest)
        val success = id != null
        val response = ArticleTypeCreateResponse(id = id, isSuccess = success)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "Update article type by id")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "ArticleTypeUpdateResponse", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ArticleTypeUpdateResponse::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @PutMapping("/update")
    fun update(
        @Positive @RequestParam("id") id: Long,
        @Valid @RequestBody articleTypeRequest: ArticleTypeRequest
    ): ResponseEntity<ArticleTypeUpdateResponse> {
        val success = articleTypeService.update(id, articleTypeRequest)
        val response = ArticleTypeUpdateResponse(success)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "Delete article type by id")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "ArticleTypeUpdateResponse", content = [Content()]),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @DeleteMapping("/delete")
    fun delete(
        @Positive @RequestParam(value = "id") id: Long
    ): ResponseEntity<Unit> {
        return try {
            articleTypeService.delete(id)
            ResponseEntity.ok().build()
        } catch (e: Exception) {
            ResponseEntity.badRequest().build()
        }
    }
}