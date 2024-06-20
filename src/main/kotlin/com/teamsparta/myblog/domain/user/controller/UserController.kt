package com.teamsparta.myblog.domain.user.controller

import com.teamsparta.myblog.domain.user.dto.LoginRequest
import com.teamsparta.myblog.domain.user.dto.LoginResponse
import com.teamsparta.myblog.domain.user.dto.SignUpRequest
import com.teamsparta.myblog.domain.user.dto.UserResponse
import com.teamsparta.myblog.domain.user.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
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
    fun signup(@RequestBody @Valid request : SignUpRequest,bindingResult: BindingResult): ResponseEntity<UserResponse> {

        if(bindingResult.hasErrors()){
            throw IllegalStateException("userName :최소 3자에서 최대 20자까지, password :최소 4자에서 최대 15까지")
        }
        return ResponseEntity.status(HttpStatus.OK).body(userService.signUpUser(request))
    }



}