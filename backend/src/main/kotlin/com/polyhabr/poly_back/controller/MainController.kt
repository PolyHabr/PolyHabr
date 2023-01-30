package com.polyhabr.poly_back.controller

import com.polyhabr.poly_back.service.MainService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api"], produces = [MediaType.APPLICATION_JSON_VALUE])
open class MainController(
    private val mainService: MainService
) {

    private val listArticlie: List<Article> = mutableListOf()

    @GetMapping("/hello")
    open fun hello(): String {
        return listOf<Article>(
            Article(
                id = "1",
                sadsad = "sadsad",
                asdsad = "asdsad",
            ),
            Article(
                id = "2",
                sadsad = "sadsad",
                asdsad = "asdsad"
            )
        ).toString()
    }
}

data class Article(

    val id: String,
    val sadsad: String,
    val asdsad: String,
)