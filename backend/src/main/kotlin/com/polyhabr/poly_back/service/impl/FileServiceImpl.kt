package com.polyhabr.poly_back.service.impl

import com.polyhabr.poly_back.controller.model.file.FileRequest
import com.polyhabr.poly_back.controller.utils.currentLogin
import com.polyhabr.poly_back.dto.FileCreatingDto
import com.polyhabr.poly_back.entity.File
import com.polyhabr.poly_back.repository.FileRepository
import com.polyhabr.poly_back.repository.auth.UsersRepository
import com.polyhabr.poly_back.service.FileService
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import javax.transaction.Transactional
import javax.validation.Valid


@Service
class FileServiceImpl(
    private val fileRepository: FileRepository,
    private val userRepository: UsersRepository,
) : FileService {

    @Transactional
    override fun create(@Valid dto: FileCreatingDto, fileName: String?): File? {
        return userRepository.findByLogin(currentLogin())?.let {
            dto.username = it.login
            val actualName = dto.name.takeIf { it != null && (it.isNotEmpty() || it.isNotBlank()) } ?: fileName
            val nameAndExtension: Pair<String, String> =
                extractNameAndExtension(actualName ?: getRandomString(15))
            val toCreate = File(
                username = dto.username,
                description = StringUtils.trimToNull(dto.description),
                name = nameAndExtension.first,
                type = nameAndExtension.second,
                data = dto.data,
                createdAt = LocalDateTime.now(),
            )
            fileRepository.save(toCreate)
        } ?: throw IllegalStateException("User not found")
    }

    private fun extractNameAndExtension(fileName: String): Pair<String, String> {
        val lastIndexOf = fileName.lastIndexOf(".")
        return fileName.substring(0, lastIndexOf) to fileName.substring(lastIndexOf + 1)
    }

    override fun findById(id: String): File? {
        return fileRepository.findByIdWithNull(id)
    }

    override fun delete(id: String) {
        userRepository.findByLogin(currentLogin())?.let {
            fileRepository.findByIdWithNull(id)?.let { file ->
                if (file.username == it.login) {
                    fileRepository.deleteById(id)
                }
            }
        }
    }

    fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}