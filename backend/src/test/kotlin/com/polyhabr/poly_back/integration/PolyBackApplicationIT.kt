package com.polyhabr.poly_back.integration

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@SpringBootTest
class PolyBackApplicationIT {

//    companion object {
//        @Container
//        val container = PostgreSQLContainer<Nothing>("postgres:12").apply {
//            withDatabaseName("postgres")
//            withUsername("postgres")
//            withPassword("postgres")
//        }
//
//        @JvmStatic
//        @DynamicPropertySource
//        fun properties(registry: DynamicPropertyRegistry) {
//            registry.add("spring.datasource.url", container::getJdbcUrl);
//            registry.add("spring.datasource.password", container::getPassword);
//            registry.add("spring.datasource.username", container::getUsername);
//        }
//    }

    @Test
    fun contextLoads() {
    }

}
