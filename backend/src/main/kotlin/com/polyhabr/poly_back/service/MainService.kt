package com.polyhabr.poly_back.service

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.springframework.stereotype.Service

@Service
open class MainService {

    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    fun hello(): String {
        return "Hello, world!"
    }
}