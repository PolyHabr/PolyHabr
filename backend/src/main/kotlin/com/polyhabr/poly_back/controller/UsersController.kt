package com.polyhabr.poly_back.controller

import com.polyhabr.poly_back.controller.model.user.request.UserRequest
import com.polyhabr.poly_back.controller.model.user.response.*
import com.polyhabr.poly_back.dto.UserDto
import com.polyhabr.poly_back.service.UsersService
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
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

@RestController
@Validated
@RequestMapping("/users")
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
                        schema = Schema(implementation = UserResponse::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @GetMapping("/byId")
    fun getById(
        @Positive @RequestParam("id") id: Long
    ): ResponseEntity<UserResponse> {
        val response = usersService
            .getById(id)
            .toResponse()
        return ResponseEntity.ok(response)
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
    fun searchUsers(
        @Schema(example = "Alex") @NotNull @NotBlank @RequestParam("prefix") prefix: String,
        @Schema(example = "0") @PositiveOrZero @RequestParam("offset") offset: Int,
        @Schema(example = "1") @Positive @RequestParam("size") size: Int,
    ): List<UserDto> {
        // TODO it's not working
        val response = usersService.search(prefix, offset, size)
        return response
    }

    @Operation(summary = "User create")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "UserCreateResponse", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = UserCreateResponse::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ]
    )
    @PostMapping("/create")
    fun create(
        @Valid @RequestBody userRequest: UserRequest
    ): ResponseEntity<UserCreateResponse> {
        val id = usersService.create(userRequest)
        val success = id != null
        val response = UserCreateResponse(id = id, isSuccess = success)
        return ResponseEntity.ok(response)
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
    fun update(
        @Positive @RequestParam("id") id: Long,
        @Valid @RequestBody userRequest: UserRequest
    ): ResponseEntity<UserUpdateResponse> {
        val success = usersService.update(id, userRequest)
        val response = UserUpdateResponse(success)
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
    fun delete(
        @Positive @RequestParam(value = "id") id: Long
    ): ResponseEntity<Unit> {
        return try {
            usersService.delete(id)
            ResponseEntity.ok().build()
        } catch (e: Exception) {
            ResponseEntity.badRequest().build()
        }
    }
}
