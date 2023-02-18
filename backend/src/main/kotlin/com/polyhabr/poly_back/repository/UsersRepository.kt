package com.polyhabr.poly_back.repository

import com.polyhabr.poly_back.entity.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface UsersRepository : JpaRepository<User, Long> {
    //    fun findByNameStartsWithIgnoreCaseOrderByName(prefix: String): List<User>
    fun findUsersByName(name: String): List<User>
}
