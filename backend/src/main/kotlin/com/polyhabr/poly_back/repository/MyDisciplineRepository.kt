package com.polyhabr.poly_back.repository

import com.polyhabr.poly_back.entity.Article
import com.polyhabr.poly_back.entity.MyDiscipline
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface MyDisciplineRepository : JpaRepository<MyDiscipline, Long> {
    @Query("SELECT md FROM MyDiscipline md WHERE md.userId.id = :id ORDER BY md.id DESC")
    fun findByUserId(@Param("id") id: Long): List<MyDiscipline>?
}