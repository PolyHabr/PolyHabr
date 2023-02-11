package com.polyhabr.poly_back.controller

import com.polyhabr.poly_back.dto.CommentDto
import com.polyhabr.poly_back.service.CommentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/comment")
class CommentController(
    private val commentService: CommentService,
) {
    @GetMapping
    fun getAll(): List<CommentDto> = commentService.getAll()
}