//package com.polyhabr.poly_back.file
//
//import com.fasterxml.jackson.core.JsonProcessingException
//import com.fasterxml.jackson.databind.ObjectMapper
//import com.polyhabr.poly_back.controller.CommentController
//import com.polyhabr.poly_back.controller.auth.AuthController
//import com.polyhabr.poly_back.controller.auth.LoginUser
//import com.polyhabr.poly_back.controller.model.file.FileOnlyRequest
//import com.polyhabr.poly_back.dto.FileCreatingDto
//import com.polyhabr.poly_back.entity.File
//import com.polyhabr.poly_back.repository.FileRepository
//import com.polyhabr.poly_back.service.FileService
//import org.junit.jupiter.api.Assertions
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.core.io.ClassPathResource
//import org.springframework.mock.web.MockMultipartFile
//import org.springframework.test.web.servlet.MockMvc
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
//import org.springframework.test.web.servlet.setup.MockMvcBuilders
//import org.springframework.util.FileCopyUtils
//import org.springframework.util.MimeTypeUtils
//import org.springframework.util.ResourceUtils
//import org.springframework.web.context.WebApplicationContext
//import java.io.IOException
//import java.nio.file.Files
//import java.time.LocalDateTime
//
//
//@SpringBootTest
//class FileControllerTest {
//    @Autowired
//    lateinit var context: WebApplicationContext
//
//    lateinit var mvc: MockMvc
//
//    @Autowired
//    lateinit var jsonMapper: ObjectMapper
//
//    @Autowired
//    lateinit var fileService: FileService
//
//    @Autowired
//    lateinit var fileRepository: FileRepository
//
//    @Autowired
//    lateinit var authController: AuthController
//
//    @BeforeEach
//    fun setUp() {
//        mvc = MockMvcBuilders.webAppContextSetup(context).build()
//        fileRepository.deleteAll()
//        fileRepository.flush()
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun create_withName_OK() {
//        val loginResponse = authController.manualLogin(
//            LoginUser(
//                username = "admin",
//                password = "admincool"
//            )
//        )
//        val dto = FileOnlyRequest(
//            name = "test.png",
//        )
//        val model = getFileModel(dto)
//        val file = createDummyFile()
//        val result = mvc.perform(
//            MockMvcRequestBuilders.multipart("/files")
//                .file(model)
//                .file(file)
//                .header("Authorization", "Bearer ${loginResponse.accessToken}")
//        )
//            .andDo(print())
//            .andExpect(status().isCreated)
//            .andReturn()
//        val parsed = jsonMapper.readValue(result.response.contentAsByteArray, File::class.java)!!
//        Assertions.assertNotNull(parsed)
//        Assertions.assertNull(parsed.data)
//        Assertions.assertEquals("test", parsed.name)
//        Assertions.assertEquals("png", parsed.type)
//        Assertions.assertNotNull(parsed.id)
//        val findFile = fileRepository.findByIdWithNull(parsed.id!!)
//        Assertions.assertNotNull(findFile)
//        Assertions.assertNotNull(findFile!!.data)
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun create_emptyProperties() {
//        val dto = FileCreatingDto(
//            name = "test.png",
//        )
//        val model = getFileModel(dto)
//        val file = createDummyFile()
//        mvc.perform(
//            MockMvcRequestBuilders.multipart("/files")
//                .file(model)
//                .file(file)
//        )
//            .andDo(print())
//            .andExpect(status().isBadRequest)
//            .andReturn()
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun create_noFile() {
//        val dto = FileCreatingDto(
//            name = "test.png",
//            username = "user@email.com",
//            description = "description"
//        )
//        val model = getFileModel(dto)
//        mvc.perform(
//            MockMvcRequestBuilders.multipart("/files")
//                .file(model)
//        )
//            .andDo(print())
//            .andExpect(status().isBadRequest)
//            .andReturn()
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun read() {
//        val toCreate = File(
//            name = "test",
//            username = "user@email.com",
//            type = "pdf",
//            createdAt = LocalDateTime.now(),
//            data = byteArrayOf()
//        )
//        val file: File = fileRepository.save(toCreate)
//        val result = mvc.perform(
//            get("/files/{id}", file.id)
//        )
//            .andDo(print())
//            .andExpect(status().isOk)
//            .andReturn()
//        val parsed: File = jsonMapper.readValue(result.response.contentAsString, File::class.java)
//        Assertions.assertEquals(file.id, parsed.id)
//        Assertions.assertEquals(file.name, parsed.name)
//        Assertions.assertEquals(file.description, parsed.description)
//        Assertions.assertEquals(file.username, parsed.username)
//        Assertions.assertEquals(file.type, parsed.type)
//        Assertions.assertNotNull(parsed.createdAt)
//        Assertions.assertNull(parsed.data)
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun read_notFound() {
//        val id = "NoSuchId"
//        mvc.perform(
//            get("/files/{id}", id)
//        )
//            .andDo(print())
//            .andExpect(status().isNotFound)
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun delete_OK() {
//        val toCreate = File(
//            name = "test",
//            username = "user@email.com",
//            type = "pdf",
//            data = byteArrayOf()
//        )
//        val file: File = fileRepository.save(toCreate)
//        mvc.perform(
//            delete("/files/{id}", file.id)
//        )
//            .andDo(print())
//            .andExpect(status().isNoContent)
//        val fileOpt = fileService.findById(file.id!!)
//        Assertions.assertNull(fileOpt)
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun download() {
//        val testFile = ResourceUtils.getFile("classpath:dummy-image.png")
//        val toCreate = File(
//            name = "test-image",
//            username = "user@email.com",
//            type = "png",
//            data = Files.readAllBytes(testFile.toPath())
//        )
//        val file: File = fileRepository.save(toCreate)
//        mvc.perform(
//            get("/files/{id}/download", file.id)
//        )
//            .andDo(print())
//            .andExpect(status().isOk)
//            .andReturn()
//    }
//
//    @Throws(JsonProcessingException::class)
//    private fun getFileModel(dto: FileOnlyRequest): MockMultipartFile {
//        return MockMultipartFile(
//            "model", "", "application/json",
//            jsonMapper.writeValueAsString(dto).toByteArray()
//        )
//    }
//
//    @Throws(IOException::class)
//    private fun createDummyFile(): MockMultipartFile {
//        val dummyAttachment = ClassPathResource("dummy-image.png")
//        Assertions.assertTrue(dummyAttachment.isReadable)
//        return MockMultipartFile(
//            "file",
//            "dummy-image.png",
//            MimeTypeUtils.IMAGE_PNG_VALUE,
//            FileCopyUtils.copyToByteArray(dummyAttachment.inputStream)
//        )
//    }
//}