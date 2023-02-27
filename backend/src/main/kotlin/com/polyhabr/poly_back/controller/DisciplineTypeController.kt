package com.polyhabr.poly_back.controller

import com.polyhabr.poly_back.controller.model.disciplineType.request.DisciplineTypeRequest
import com.polyhabr.poly_back.controller.model.disciplineType.response.*
import com.polyhabr.poly_back.service.DisciplineTypeService
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

@RestController
@Validated
@RequestMapping("/discipline_type")
class DisciplineTypeController(
    private val disciplineTypeService: DisciplineTypeService,
) {
    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleConstraintViolationException(e: ConstraintViolationException): ResponseEntity<String?>? {
        return ResponseEntity("not valid due to validation error: " + e.message, HttpStatus.BAD_REQUEST)
    }
    @Operation(summary = "Discipline type list")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Discipline type list", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = DisciplineTypeListResponse::class)
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
        principal: Principal
    ): ResponseEntity<DisciplineTypeListResponse> {
        val rawResponse = disciplineTypeService
            .getAll(
                offset = offset,
                size = size,
            )
        return ResponseEntity.ok(rawResponse.toListResponse())
    }

    @Operation(summary = "Discipline type find by id")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "DisciplineType", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = DisciplineTypeResponse::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @GetMapping("/byId")
    fun getById(
        @Positive @RequestParam("id") id: Long
    ): ResponseEntity<DisciplineTypeResponse> {
        val response = disciplineTypeService
            .getById(id)
            .toResponse()
        return response.let {
            ResponseEntity.ok(response)
        }
    }

    @Operation(summary = "Search discipline types by prefix")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "DisciplineType", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = DisciplineTypeListResponse::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @GetMapping("/search")
    fun searchDisciplineTypesByName(
        @Schema(example = "physics") @RequestParam("prefix") prefix: String?,
        @Schema(example = "0") @PositiveOrZero @RequestParam("offset") offset: Int,
        @Schema(example = "1") @Positive @RequestParam("size") size: Int,
    ): ResponseEntity<DisciplineTypeListResponse> {
        val rawResponse = disciplineTypeService
            .searchByName(prefix, offset, size)
        return ResponseEntity.ok(rawResponse.toListResponse())
    }

    @Operation(summary = "Discipline type create")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "DisciplineTypeCreateResponse", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = DisciplineTypeCreateResponse::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    fun create(
        @Valid @RequestBody disciplineTypeRequest: DisciplineTypeRequest
    ): ResponseEntity<DisciplineTypeCreateResponse> {
        val id = disciplineTypeService.create(disciplineTypeRequest)
        val success = id != null
        val response = DisciplineTypeCreateResponse(id = id, isSuccess = success)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "Update discipline type by id")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "DisciplineTypeUpdateResponse", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = DisciplineTypeUpdateResponse::class)
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
        @Valid @RequestBody disciplineTypeRequest: DisciplineTypeRequest
    ): ResponseEntity<DisciplineTypeUpdateResponse> {
        val (success, message) = disciplineTypeService.update(id, disciplineTypeRequest)
        val response = DisciplineTypeUpdateResponse(success, message)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "Delete discipline type by id")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "DisciplineTypeUpdateResponse", content = [Content()]),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    fun delete(
        @Positive @RequestParam(value = "id") id: Long
    ): ResponseEntity<Unit> {
        return try {
            disciplineTypeService.delete(id)
            ResponseEntity.ok().build()
        } catch (e: Exception) {
            ResponseEntity.badRequest().build()
        }
    }
}