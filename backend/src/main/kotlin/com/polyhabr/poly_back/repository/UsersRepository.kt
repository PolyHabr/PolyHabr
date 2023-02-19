package com.polyhabr.poly_back.repository

import com.polyhabr.poly_back.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestParam

interface UsersRepository : JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.name LIKE %:pname%")
    fun findUsersByName(pageable: Pageable, @Param("pname") pname: String): Page<User>
}
