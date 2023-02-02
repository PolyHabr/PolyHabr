package com.polyhabr.poly_back.controller

import com.polyhabr.poly_back.dto.TagTypeDto
import com.polyhabr.poly_back.service.TagTypeService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tag_type")
class TagTypeController(
    private val tagTypeService: TagTypeService,
) {
    @GetMapping
    fun getAll(): List<TagTypeDto> = tagTypeService.getAll()
}