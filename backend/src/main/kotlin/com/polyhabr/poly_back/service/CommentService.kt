package com.polyhabr.poly_back.service

import com.polyhabr.poly_back.dto.CommentDto

interface CommentService {
    fun getAll(): List<CommentDto>

}