package com.polyhabr.poly_back.service.impl

import com.polyhabr.poly_back.controller.model.user.request.UserRequest
import com.polyhabr.poly_back.controller.model.user.request.UserUpdateRequest
import com.polyhabr.poly_back.controller.model.user.request.toDto
import com.polyhabr.poly_back.controller.utils.currentLogin
import com.polyhabr.poly_back.dto.UserDto
import com.polyhabr.poly_back.entity.auth.User
import com.polyhabr.poly_back.repository.auth.UsersRepository
import com.polyhabr.poly_back.service.UsersService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*
import kotlin.RuntimeException

@Service
class UsersServiceImpl(
    private val usersRepository: UsersRepository
) : UsersService {

    @Autowired
    lateinit var encoder: PasswordEncoder
    override fun getAll(
        offset: Int,
        size: Int,
    ): Page<UserDto> {
        return usersRepository
            .findAll(
                PageRequest.of(
                    offset,
                    size,
                )
            )
            .map { it.toDto() }
    }

    override fun getById(id: Long): UserDto? {
        return usersRepository.findByIdOrNull(id)
            ?.toDto()
    }

    override fun searchByName(prefix: String?, offset: Int, size: Int): Page<UserDto> =
        usersRepository
            .findUsersByName(
                PageRequest.of(
                    offset,
                    size,
                ), prefix ?: ""
            )
            .map { it.toDtoWithoutPasswordAndEmail() }

    override fun create(userRequest: UserRequest): Long? {
        return usersRepository.save(
            userRequest
                .toDto()
                .toEntity()
        ).id
    }

    override fun update(userRequest: UserUpdateRequest): Pair<Boolean, String> {
        usersRepository.findByLogin(currentLogin())?.let { currentUser ->
            currentUser.apply {
                userRequest.email?.let { email = it }
                userRequest.name?.let { name = it }
                userRequest.surname?.let { surname = it }
                userRequest.password?.let { password = encoder.encode(it) }
            }
            return usersRepository.save(currentUser).id?.let { true to "Ok" } ?: (false to "Error while update")
        } ?: return false to "User not found"
    }

    override fun delete(): Pair<Boolean, String> {
        return try {
            usersRepository.findByLogin(currentLogin())?.let { currentUser ->
                currentUser.id?.let { id ->
                    usersRepository.deleteById(id)
                    true to "User deleted"
                } ?: (false to "User id not found")
            } ?: (false to "User not found")
        } catch (e: Exception) {
            false to "Internal server error"
        }
    }

    private fun User.toDto() = UserDto(
        id = this.id,
        email = this.email,
        login = this.login,
        name = this.name,
        password = this.password,
        surname = this.surname,
    )

    private fun User.toDtoWithoutPasswordAndEmail() = UserDto(
        id = this.id,
        login = this.login,
        name = this.name,
        surname = this.surname,
    )

    private fun UserDto.toEntity() = User(
        email = this.email!!,
        login = this.login!!,
        name = this.name!!,
        password = this.password!!,
        surname = this.surname!!,
    )
}