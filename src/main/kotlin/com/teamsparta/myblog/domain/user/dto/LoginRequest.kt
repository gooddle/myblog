package com.teamsparta.myblog.domain.user.dto

data class LoginRequest(
    val email: String,
    val password: String,
)