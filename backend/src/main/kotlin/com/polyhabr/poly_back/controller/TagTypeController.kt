package com.polyhabr.poly_back.controller

import com.polyhabr.poly_back.controller.model.tagType.request.TagTypeRequest
import com.polyhabr.poly_back.controller.model.tagType.response.*
import com.polyhabr.poly_back.service.TagTypeService
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
import java.security.Principal
import javax.validation.ConstraintViolationException
import javax.validation.Valid
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

@CrossOrigin(origins = ["*"], maxAge = 14400)
@RestController
@Validated
@RequestMapping("/tag_type")
class TagTypeController(
    private val tagTypeService: TagTypeService,
) {
    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleConstraintViolationException(e: ConstraintViolationException): ResponseEntity<String?>? {
        return ResponseEntity("not valid due to validation error: " + e.message, HttpStatus.BAD_REQUEST)
    }
    @Operation(summary = "Tag type list")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Tag type list", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = TagTypeListResponse::class)
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
        
    ): ResponseEntity<TagTypeListResponse> {
        val rawResponse = tagTypeService
            .getAll(
                offset = offset,
                size = size,
            )
        return ResponseEntity.ok(rawResponse.toListResponse())
    }

    @Operation(summary = "Tag type find by id")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "TagType", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = TagTypeResponse::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @GetMapping("/byId")
    fun getById(
        @Positive @RequestParam("id") id: Long
    ): ResponseEntity<TagTypeResponse> {
        val response = tagTypeService
            .getById(id)
            .toResponse()
        return response.let {
            ResponseEntity.ok(response)
        }
    }

    @Operation(summary = "Search tag types by prefix")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "TagType", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = TagTypeListResponse::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @GetMapping("/search")
    fun searchTagTypesByName(
        @Schema(example = "physics") @RequestParam("prefix") prefix: String?,
        @Schema(example = "0") @PositiveOrZero @RequestParam("offset") offset: Int,
        @Schema(example = "1") @Positive @RequestParam("size") size: Int,
    ): ResponseEntity<TagTypeListResponse> {
        val rawResponse = tagTypeService
            .searchByName(prefix?.lowercase(), offset, size)
        return ResponseEntity.ok(rawResponse.toListResponse())
    }

    @Operation(summary = "Tag type create")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "TagTypeCreateResponse", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = TagTypeCreateResponse::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    fun create(
        @Valid @RequestBody tagTypeRequest: TagTypeRequest
    ): ResponseEntity<TagTypeCreateResponse> {
        val id = tagTypeService.create(tagTypeRequest)
        val success = id != null
        val response = TagTypeCreateResponse(id = id, isSuccess = success)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "Update tag type by id")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "TagTypeUpdateResponse", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = TagTypeUpdateResponse::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    fun update(
        @Positive @RequestParam("id") id: Long,
        @Valid @RequestBody tagTypeRequest: TagTypeRequest
    ): ResponseEntity<TagTypeUpdateResponse> {
        val (success, message) = tagTypeService.update(id, tagTypeRequest)
        val response = TagTypeUpdateResponse(success, message)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "Delete tag type by id")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "TagTypeUpdateResponse", content = [Content()]),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    fun delete(
        @Positive @RequestParam(value = "id") id: Long
    ): ResponseEntity<String> {
        val (success, message) = tagTypeService.delete(id)
        return if (success) {
            ResponseEntity.ok().body(message)
        } else {
            ResponseEntity.internalServerError().body(message)
        }
    }
}