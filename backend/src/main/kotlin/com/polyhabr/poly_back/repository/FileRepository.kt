package com.polyhabr.poly_back.repository

import com.polyhabr.poly_back.entity.File
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface FileRepository : JpaRepository<File, String> {
    fun findByUsername(@Param("username") username: String): List<File>

    @Query("SELECT f FROM File f WHERE f.id = :id")
    fun findByIdWithNull(@Param("id") id: String): File?
}