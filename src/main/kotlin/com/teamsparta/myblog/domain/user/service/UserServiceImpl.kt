package com.teamsparta.myblog.domain.user.service

import com.teamsparta.myblog.domain.user.dto.LoginRequest
import com.teamsparta.myblog.domain.user.dto.LoginResponse
import com.teamsparta.myblog.domain.user.dto.SignUpRequest
import com.teamsparta.myblog.domain.user.dto.UserResponse
import com.teamsparta.myblog.domain.user.model.User
import com.teamsparta.myblog.domain.user.model.toResponse
import com.teamsparta.myblog.domain.user.repository.UserRepository
import com.teamsparta.myblog.infra.security.jwt.JwtPlugin
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtPlugin: JwtPlugin,
): UserService {
    override fun loginUser(request: LoginRequest):LoginResponse{
        val user = userRepository.findByUserName(request.userName) ?: throw  IllegalStateException("없는 user 입니다")

        if(!passwordEncoder.matches(request.password, user.password)) throw IllegalStateException("비밀번호가 틀립니다.")

        if(request.password != request.password2th) throw IllegalStateException("2개의 비밀번호가 일치하지 않습니다.")
        return LoginResponse(
            token = jwtPlugin.generateAccessToken(
                subject = user.id.toString(),
                userName = user.userName
            )
        )
    }


    override fun signUpUser(request: SignUpRequest): UserResponse {
        if(userRepository.existsByUserName(request.userName))
            throw  IllegalStateException("이미 사용중인 이름입니다.")

        val passwordPattern = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#\$%^&*\\-_])[A-Za-z\\d!@#\$%^&*\\-_]$".toRegex()

        if(!request.password.matches(passwordPattern))
            throw  IllegalStateException("대문자 소문자 특수 기호로 이루어진 비밀번호로 작성해주세요")

        if (request.password.contains(request.userName))
            throw IllegalStateException("비밀번호와 닉네임은 동일하거나 포함될 수 없습니다.")


        return userRepository.save(
            User(
                userName = request.userName,
                password = passwordEncoder.encode(request.password),
            )
        ).toResponse()
    }
}