package com.teamsparta.myblog.domain.user.repository

import com.teamsparta.myblog.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {
    fun existsByUserName(userName: String): Boolean
    fun findByUserName(userName: String): User?
}