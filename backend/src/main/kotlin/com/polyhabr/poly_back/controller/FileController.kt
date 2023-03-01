package com.polyhabr.poly_back.controller

import com.polyhabr.poly_back.controller.model.file.FileOnlyRequest
import com.polyhabr.poly_back.controller.model.file.PdfRequest
import com.polyhabr.poly_back.controller.model.file.toDto
import com.polyhabr.poly_back.service.FileService
import com.polyhabr.poly_back.utility.Utility
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.io.IOException
import javax.validation.constraints.Positive

@CrossOrigin(origins = ["*"], maxAge = 14400)
@RestController
@Validated
@RequestMapping("/files")
class FileController(
    private val fileService: FileService,
) {

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "A File was successfully created.")
        ]
    )
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    fun create(
        @ModelAttribute pdfRequest: PdfRequest
    ): ResponseEntity<*>? {
        val model = FileOnlyRequest(
            name = Utility.getRandomString(30)
        )
        try {
            model.data = pdfRequest.file.bytes
        } catch (e: IOException) {
            return ResponseEntity.badRequest().build<Any>()
        }
        val fileCreated =
            fileService.createPdfForArticle(model.toDto(), pdfRequest.file.originalFilename, pdfRequest.articleId)
        fileCreated?.let {
            return ResponseEntity.status(HttpStatus.CREATED).body<Any>(it)
        } ?: return ResponseEntity.badRequest().build<Any>()
    }

    @PostMapping("/savePreviewPic", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "A File was successfully created.")
        ]
    )
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    fun createPreviewPng(
        @ModelAttribute pdfRequest: PdfRequest
    ): ResponseEntity<*>? {
        val model = FileOnlyRequest(
            name = Utility.getRandomString(30)
        )
        try {
            model.data = pdfRequest.file.bytes
        } catch (e: IOException) {
            return ResponseEntity.badRequest().build<Any>()
        }
        val fileCreated = fileService.createPreviewPicForArticle(
            model.toDto(),
            pdfRequest.file.originalFilename,
            pdfRequest.articleId
        )
        fileCreated?.let {
            return ResponseEntity.status(HttpStatus.CREATED).body<Any>(it)
        } ?: return ResponseEntity.badRequest().build<Any>()
    }

    @GetMapping("/{id}")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "File found."),
            ApiResponse(responseCode = "404", description = "File not found.")
        ]
    )
    fun read(@PathVariable("id") id: String): ResponseEntity<*>? {
        return fileService.findById(id)?.let { file ->
            ResponseEntity.ok(file)
        } ?: ResponseEntity.notFound().build<Any>()
    }

    @GetMapping("/{id}/download")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "File downloaded."),
            ApiResponse(responseCode = "404", description = "File not found.")
        ]
    )
    fun download(@PathVariable("id") id: String): ResponseEntity<ByteArray?>? {
        return fileService.findById(id)?.let { file ->
            return ResponseEntity.ok()
                .header(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + file.name + "." + file.type + "\""
                )
                .body(file.data)
        } ?: ResponseEntity.notFound().build()
    }

    @DeleteMapping
    @ApiResponses(
        value = [ApiResponse(responseCode = "200", description = "A File was successfully deleted.")]
    )
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    fun delete(
        @RequestParam("idFile") fildId: String,
        @RequestParam("idArticle") @Positive articleId: Long,
    ): ResponseEntity<*>? {
        fileService.delete(fildId, articleId)
        return ResponseEntity<Any?>(HttpStatus.NO_CONTENT)
    }
}