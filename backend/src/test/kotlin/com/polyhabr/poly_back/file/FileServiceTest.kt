package com.polyhabr.poly_back.file

import com.polyhabr.poly_back.dto.FileCreatingDto
import com.polyhabr.poly_back.repository.FileRepository
import com.polyhabr.poly_back.service.FileService
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class FileServiceTest {
    @Autowired
    lateinit var underTest: FileService

    @Autowired
    lateinit var fileRepository: FileRepository

    @BeforeEach
    fun setup() {
        fileRepository.deleteAll()
        fileRepository.flush()
    }

    @Test
    fun create_withName() {
        val name = "test"
        val type = "png"
        val dto = FileCreatingDto(
            username = "user@email.com",
            name = "$name.$type",
            description = "description",
            data = byteArrayOf()
        )

        val file = underTest.create(dto, "name.zip")!!
        Assertions.assertEquals(dto.username, file.username)
        Assertions.assertEquals(dto.description, file.description)
        Assertions.assertEquals(dto.data, file.data)
        Assertions.assertEquals(name, file.name)
        Assertions.assertEquals(type, file.type)
        Assertions.assertNotNull(file.createdAt)
    }

    @Test
    fun create_withFileName() {
        val name = "name"
        val type = "zip"
        val dto = FileCreatingDto(
            username = "user@email.com",
            description = "description",
            data = byteArrayOf()
        )
        val file = underTest.create(dto, "$name.$type")!!
        Assertions.assertEquals(dto.username, file.username)
        Assertions.assertEquals(dto.description, file.description)
        Assertions.assertEquals(dto.data, file.data)
        Assertions.assertEquals(name, file.name)
        Assertions.assertEquals(type, file.type)
        Assertions.assertNotNull(file.createdAt)
    }
}
