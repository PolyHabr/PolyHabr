package com.polyhabr.poly_back.controller

import com.polyhabr.poly_back.dto.ArticleTypeDto
import com.polyhabr.poly_back.service.ArticleTypeService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/article_types")
class AticleTypeController(
    private val articleTypeService: ArticleTypeService,
) {
    @GetMapping
    fun getAll(): List<ArticleTypeDto> = articleTypeService.getAll()
}