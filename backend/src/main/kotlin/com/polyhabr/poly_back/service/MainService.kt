package com.polyhabr.poly_back.service

import org.springframework.stereotype.Service

@Service
open class MainService {
    fun hello(): String {
        return "Hello, world!"
    }
}