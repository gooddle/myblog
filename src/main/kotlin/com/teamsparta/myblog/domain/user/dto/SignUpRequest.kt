package com.teamsparta.myblog.domain.user.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size


data class SignUpRequest(

    @field:NotBlank
    @field:Size(min = 3, max = 20)
    val userName: String,

    @field:NotBlank
    @field:Size(min = 4, max = 15)
    val password: String,

    val emailCode :String
)