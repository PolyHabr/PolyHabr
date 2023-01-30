package com.polyhabr.poly_back

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication()
open class PolyBackApplication

fun main(args: Array<String>) {
    runApplication<PolyBackApplication>(*args)
}
