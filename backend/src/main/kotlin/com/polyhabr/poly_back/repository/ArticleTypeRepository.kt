package com.polyhabr.poly_back.repository

import com.polyhabr.poly_back.entity.ArticleType
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleTypeRepository: JpaRepository<ArticleType, Long>
