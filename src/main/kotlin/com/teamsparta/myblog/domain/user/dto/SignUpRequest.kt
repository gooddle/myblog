package com.teamsparta.myblog.domain.user.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern


data class SignUpRequest(

    @field:NotBlank
    @field:Email
    val email: String,

    @field:NotBlank
    @field:Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#\$%^&*\\-_]).{4,}$")
    val password: String,

    val emailCode :String
)