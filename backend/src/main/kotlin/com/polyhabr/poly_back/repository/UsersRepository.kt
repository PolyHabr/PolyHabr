package com.polyhabr.poly_back.repository

import com.polyhabr.poly_back.entity.Users
import org.springframework.data.jpa.repository.JpaRepository

interface UsersRepository:  JpaRepository<Users, Int> {
}