package com.polyhabr.poly_back.repository.auth

import com.polyhabr.poly_back.entity.auth.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface RoleRepository : JpaRepository<Role, Long> {

    fun findByName(@Param("name") name: String): Role
}