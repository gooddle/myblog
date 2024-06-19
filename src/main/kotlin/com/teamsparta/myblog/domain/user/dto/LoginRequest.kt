package com.teamsparta.myblog.domain.user.dto

data class LoginRequest(
    val userName: String,
    val password: String
)