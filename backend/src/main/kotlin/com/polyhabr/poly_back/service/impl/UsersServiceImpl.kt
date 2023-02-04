package com.polyhabr.poly_back.service.impl

import com.polyhabr.poly_back.dto.UsersDto
import com.polyhabr.poly_back.entity.Users
import com.polyhabr.poly_back.repository.UsersRepository
import com.polyhabr.poly_back.service.UsersService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import kotlin.RuntimeException

@Service
class UsersServiceImpl(
    private val usersRepository: UsersRepository
) : UsersService {
    override fun getAll(): List<UsersDto> {
        return usersRepository.findAll().map {
            it.toDto()
        }
    }

    override fun getById(id: Int): UsersDto {
        return usersRepository.findByIdOrNull(id)
            ?.toDto()
            ?: throw RuntimeException("User not found")
    }

    override fun search(prefix: String): List<UsersDto> =
        usersRepository.findByNameStartsWithIgnoreCaseOrderByName(prefix)
            .map { it.toDto() }

    override fun create(dto: UsersDto): Int {
        return usersRepository.save(dto.toEntity()).id
    }

    override fun update(id: Int, dto: UsersDto) {
        val existingUser = usersRepository.findByIdOrNull(id)
            ?: throw RuntimeException("User not found")
        existingUser.name = dto.name

        usersRepository.save(existingUser)
    }

    override fun delete(id: Int) {
        val existingUser = usersRepository.findByIdOrNull(id)
            ?: throw RuntimeException("User not found")

        usersRepository.deleteById(existingUser.id)
    }

    private fun Users.toDto(): UsersDto =
        UsersDto(
            id = this.id,
            email = this.email,
            login = this.login,
            name = this.name,
            password = this.password,
            surname = this.surname,
        )

    private fun UsersDto.toEntity(): Users =
        Users(
            id = 0,
            email = this.email,
            login = this.login,
            name = this.name,
            password = this.password,
            surname = this.surname,
            user_id = null,
            user_to_liked_article = null,
            users_comment = null
        )
}