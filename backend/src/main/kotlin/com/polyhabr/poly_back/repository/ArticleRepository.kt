package com.polyhabr.poly_back.repository

import com.polyhabr.poly_back.entity.Article
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleRepository: JpaRepository<Article, Int> {

}