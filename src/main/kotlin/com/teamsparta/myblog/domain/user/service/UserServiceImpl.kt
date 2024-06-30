package com.teamsparta.myblog.domain.user.service


import com.teamsparta.myblog.domain.common.RedisUtils
import com.teamsparta.myblog.domain.user.dto.LoginRequest
import com.teamsparta.myblog.domain.user.dto.LoginResponse
import com.teamsparta.myblog.domain.user.dto.SignUpRequest
import com.teamsparta.myblog.domain.user.dto.UserResponse
import com.teamsparta.myblog.domain.user.model.User
import com.teamsparta.myblog.domain.user.model.toResponse
import com.teamsparta.myblog.domain.user.repository.UserRepository
import com.teamsparta.myblog.infra.aop.NotFoundException
import com.teamsparta.myblog.infra.security.jwt.JwtPlugin
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtPlugin: JwtPlugin,
    private val redisUtils: RedisUtils,

    ): UserService {
    override fun loginUser(request: LoginRequest): LoginResponse {
        val user = userRepository.findByUserName(request.userName)
            ?: throw NotFoundException("없는 user 입니다")

        if (!passwordEncoder.matches(request.password, user.password))
            throw NotFoundException("비밀번호가 틀립니다.")

        if (request.password != request.password2th) throw NotFoundException("2개의 비밀번호가 일치하지 않습니다.")

        return LoginResponse(
            token = jwtPlugin.generateAccessToken(
                subject = user.id.toString(),
                userName = user.userName
            )
        )
    }

    @Transactional
    override fun signUpUser(request: SignUpRequest): UserResponse {
        //발급 받은 인증 코드와 회원가입 시 기입한 코드 비교
        checkEmail(request.emailCode)

        if (userRepository.existsByUserName(request.userName))
            throw NotFoundException("이미 사용중인 이름입니다.")

        val passwordPattern = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#\$%^&*\\-_]).{4,}$".toRegex()

        if (!request.password.matches(passwordPattern))
            throw NotFoundException("대문자 소문자 특수 기호로 이루어진 비밀번호로 작성해주세요")

        if (request.password.contains(request.userName))
            throw NotFoundException("비밀번호와 닉네임은 동일하거나 포함될 수 없습니다.")

        //회원가입이 끝나면 redis에서 코드 삭제
        redisUtils.deleteData(request.emailCode)


        return userRepository.save(
            User(
                userName = request.userName,
                password = passwordEncoder.encode(request.password),
            )
        ).toResponse()

    }


    //발급 받은 인증 코드
   private fun checkEmail(codeNumber: String):Boolean {
        redisUtils.getData(codeNumber)
            ?: throw NotFoundException("인증번호가 잘못되었거나 인증 시간이 초과되었습니다. 다시 확인해주세요.")
        return true
    }



}