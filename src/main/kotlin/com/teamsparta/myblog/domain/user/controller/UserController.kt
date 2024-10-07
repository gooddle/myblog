package com.teamsparta.myblog.domain.user.controller

import com.teamsparta.myblog.domain.user.dto.*
import com.teamsparta.myblog.domain.user.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RequestMapping("/api/v1/users")
@RestController
class UserController(
    private val userService: UserService
) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(userService.loginUser(request))
    }


    @PostMapping("/signup")
    fun signup(@RequestBody @Valid request : SignUpRequest): ResponseEntity<UserResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(userService.signUpUser(request))
    }

}