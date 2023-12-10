package com.polyhabr.poly_back.controller

import com.polyhabr.poly_back.controller.model.comment.request.CommentRequest
import com.polyhabr.poly_back.controller.model.comment.response.*
import com.polyhabr.poly_back.service.CommentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.ConstraintViolationException
import javax.validation.Valid
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero
import javax.xml.stream.events.Comment

@CrossOrigin(origins = ["*"], maxAge = 14400)
@RestController
@Validated
@RequestMapping("/comment")
class CommentController(
    private val commentService: CommentService,
) {

    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleConstraintViolationException(e: ConstraintViolationException): ResponseEntity<String?>? {
        return ResponseEntity("not valid due to validation error: " + e.message, HttpStatus.BAD_REQUEST)
    }

    @Operation(summary = "Comment list")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Comment list", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = CommentListResponse::class)
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

        ): ResponseEntity<CommentListResponse> {
        val rawResponse = commentService
            .getAll(
                offset = offset,
                size = size,
            )
        return ResponseEntity.ok(rawResponse.toListResponse())
    }

    @Operation(summary = "Comment list by article id")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Comment list", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = CommentListResponse::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @GetMapping("/byArticleId")
    fun getByArticleId(
        @Schema(example = "0") @PositiveOrZero @RequestParam("offset") offset: Int,
        @Schema(example = "1") @Positive @RequestParam("size") size: Int,
        @Positive @RequestParam("articleId") id: Long
    ): ResponseEntity<CommentListResponse> {
        val rawResponse = commentService
            .getByArticleIdAll(
                offset = offset,
                size = size,
                articleId = id
            )
        return ResponseEntity.ok(rawResponse.toListResponse())
    }


    @Operation(summary = "Comment find by id")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Comment", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = CommentResponse::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @GetMapping("/byId")
    fun getById(
        @Positive @RequestParam("id") id: Long
    ): ResponseEntity<CommentResponse> {
        val response = commentService
            .getById(id)
            .toResponse()
        return response.let {
            ResponseEntity.ok(response)
        }
    }


    @Operation(summary = "Search comments by prefix")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Comment", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = CommentListResponse::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @GetMapping("/search")
    fun searchCommentsByName(
        @Schema(example = "Nice") @RequestParam("prefix") prefix: String?,
        @Schema(example = "0") @PositiveOrZero @RequestParam("offset") offset: Int,
        @Schema(example = "1") @Positive @RequestParam("size") size: Int,
    ): ResponseEntity<CommentListResponse> {
        val rawResponse = commentService
            .searchByName(prefix?.lowercase(), offset, size)
        return ResponseEntity.ok(rawResponse.toListResponse())
    }

    @Operation(summary = "Comment create")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "CommentCreateResponse", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = CommentResponse::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @PostMapping("/create")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    fun create(
        @Valid @RequestBody commentRequest: CommentRequest
    ): ResponseEntity<CommentResponse> {
        val comment = commentService.create(commentRequest)
        return ResponseEntity.ok(comment.toResponse())
    }

    @Operation(summary = "Update comment by id")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "CommentUpdateResponse", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = CommentUpdateResponse::class)
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
        @Valid @RequestBody commentRequest: CommentRequest
    ): ResponseEntity<CommentUpdateResponse> {
        val (success, message) = commentService.update(id, commentRequest)
        val response = CommentUpdateResponse(success, message)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "Delete comment by id")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "CommentUpdateResponse", content = [Content()]),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    fun delete(
        @Positive @RequestParam(value = "id") id: Long
    ): ResponseEntity<String> {
        val (success, message) = commentService.delete(id)
        return if (success) {
            ResponseEntity.ok().body(message)
        } else {
            ResponseEntity.internalServerError().body(message)
        }
    }
}