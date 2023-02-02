package com.polyhabr.poly_back.controller

import com.polyhabr.poly_back.dto.ArticleDto
import com.polyhabr.poly_back.service.ArticleService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/articles")
class ArticleController(
    private val articleService: ArticleService,
) {

    @GetMapping
    fun getAll(): List<ArticleDto> = articleService.getAll()

}