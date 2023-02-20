package com.polyhabr.poly_back.service.impl

import com.polyhabr.poly_back.controller.model.user.request.UserRequest
import com.polyhabr.poly_back.controller.model.user.request.toDto
import com.polyhabr.poly_back.dto.UserDto
import com.polyhabr.poly_back.entity.auth.User
import com.polyhabr.poly_back.repository.auth.UsersRepository
import com.polyhabr.poly_back.service.UsersService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import kotlin.RuntimeException

@Service
class UsersServiceImpl(
    private val usersRepository: UsersRepository
) : UsersService {
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

    override fun getById(id: Long): UserDto {
        return usersRepository.findByIdOrNull(id)
            ?.toDto()
            ?: throw RuntimeException("User not found")
    }

    override fun searchByName(prefix: String?, offset: Int, size: Int): Page<UserDto> =
        usersRepository
            .findUsersByName(
                PageRequest.of(
                    offset,
                    size,
                ), prefix ?: ""
            )
            .map { it.toDto() }

    override fun create(userRequest: UserRequest): Long? {
        return usersRepository.save(
            userRequest
                .toDto()
                .toEntity()
        ).id
    }

    override fun update(id: Long, userRequest: UserRequest): Boolean {
        val existingUser = usersRepository.findByIdOrNull(id)
            ?: throw RuntimeException("User not found")
        existingUser.name = userRequest.name ?: throw RuntimeException("name not found")

        return usersRepository.save(existingUser).id?.let { true } ?: false
    }

    override fun delete(id: Long) {
        val existingUser = usersRepository.findByIdOrNull(id)
            ?: throw RuntimeException("User not found")
        val existedId = existingUser.id ?: throw RuntimeException("id not found")
        usersRepository.deleteById(existedId)
    }

    private fun User.toDto() = UserDto(
        id = this.id,
        email = this.email,
        login = this.login,
        name = this.name,
        password = this.password,
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