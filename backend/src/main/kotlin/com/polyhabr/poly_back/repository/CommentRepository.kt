package com.polyhabr.poly_back.repository

import com.polyhabr.poly_back.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository: JpaRepository<Comment, Int> {

}