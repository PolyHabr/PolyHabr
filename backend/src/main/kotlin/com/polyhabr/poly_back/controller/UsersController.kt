package com.polyhabr.poly_back.controller

import com.polyhabr.poly_back.controller.model.user.request.UserUpdateRequest
import com.polyhabr.poly_back.controller.model.user.response.*
import com.polyhabr.poly_back.service.UsersService
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

@RestController
@Validated
@RequestMapping("/users")
@CrossOrigin(origins = ["*"], maxAge = 3600)
class UsersController(
    private val usersService: UsersService,
) {
    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleConstraintViolationException(e: ConstraintViolationException): ResponseEntity<String?>? {
        return ResponseEntity("not valid due to validation error: " + e.message, HttpStatus.BAD_REQUEST)
    }

    @Operation(summary = "User list")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "User list", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = UserListResponse::class)
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
    ): ResponseEntity<UserListResponse> {
        val rawResponse = usersService
            .getAll(
                offset = offset,
                size = size,
            )
        return ResponseEntity.ok(rawResponse.toListResponse())
    }

    @Operation(summary = "User find by id")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "User", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = UserMeResponse::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @GetMapping("/byId")
    fun getById(
        @Positive @RequestParam("id") id: Long
    ): ResponseEntity<UserOtherResponse>? {
        val response = usersService
            .getById(id)
            ?.toOtherResponse()
        return response?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.badRequest().build()
    }

    @Operation(summary = "Search users by prefix")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "User", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = UserListResponse::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @GetMapping("/search")
    fun searchUsersByName(
        @Schema(example = "Alex") @RequestParam("prefix") prefix: String?,
        @Schema(example = "0") @PositiveOrZero @RequestParam("offset") offset: Int,
        @Schema(example = "1") @Positive @RequestParam("size") size: Int,
    ): ResponseEntity<UserListResponse> {
        val rawResponse = usersService
            .searchByName(prefix, offset, size)
        return ResponseEntity.ok(rawResponse.toListResponse())
    }

    @Operation(summary = "Update user by id")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "UserUpdateResponse", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = UserUpdateResponse::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @PutMapping("/update")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    fun update(
        @Valid @RequestBody userRequest: UserUpdateRequest,
    ): ResponseEntity<UserUpdateResponse> {
        val (success, message) = usersService.update(userRequest)
        val response = UserUpdateResponse(success, message)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "Delete user by id")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "UserUpdateResponse", content = [Content()]),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    fun delete(): ResponseEntity<String> {
        val (success, message) = usersService.delete()
        return if (success) {
            ResponseEntity.ok().body(message)
        } else {
            ResponseEntity.internalServerError().body(message)
        }
    }
}
