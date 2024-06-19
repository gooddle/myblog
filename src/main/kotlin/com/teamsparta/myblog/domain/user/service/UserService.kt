package com.teamsparta.myblog.domain.user.service

import com.teamsparta.myblog.domain.user.dto.LoginRequest
import com.teamsparta.myblog.domain.user.dto.LoginResponse
import com.teamsparta.myblog.domain.user.dto.SignUpRequest
import com.teamsparta.myblog.domain.user.dto.UserResponse

interface UserService {

    fun loginUser(request : LoginRequest) : LoginResponse
    fun signUpUser(request: SignUpRequest): UserResponse

}