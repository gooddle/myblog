package com.teamsparta.myblog.domain.user.controller

import com.teamsparta.myblog.domain.user.dto.*
import com.teamsparta.myblog.domain.user.service.UserService
import com.teamsparta.myblog.infra.aop.NotFoundException
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
    fun login(@RequestBody request: LoginRequest): ResponseEntity<ApiUserResponse<LoginResponse>> {
       return  try {
           val user =userService.loginUser(request)
           val response = ApiUserResponse("로그인 성공 ",HttpStatus.OK.value(),user)
           ResponseEntity.status(HttpStatus.OK).body(response)
       }

       catch (e:NotFoundException){
           ResponseEntity.badRequest().body(ApiUserResponse.error(e.message))
       }
    }


    @PostMapping("/signup")
    fun signup(@RequestBody @Valid request : SignUpRequest,bindingResult: BindingResult): ResponseEntity<ApiUserResponse<UserResponse>> {
    return try {

        if(bindingResult.hasErrors()){
            throw IllegalStateException("userName :최소 3자에서 최대 20자까지, password :최소 4자에서 최대 15까지")
        }

        userService.signUpUser(request)
        ResponseEntity.status(HttpStatus.ACCEPTED)
            .body(ApiUserResponse.success("회원가입 성공",HttpStatus.OK.value(),null))
    }

         catch (e:NotFoundException){
            ResponseEntity.badRequest().body(ApiUserResponse.error(e.message))
    }


    }

}