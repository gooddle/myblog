package com.teamsparta.myblog.domain.email.controller

import com.teamsparta.myblog.domain.email.dto.SendEmailRequest
import com.teamsparta.myblog.domain.email.service.EmailService
import org.springframework.http.HttpStatus

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/v1")
@RestController
class EmailCodeController(
    private val emailService: EmailService
) {

    @PostMapping("/send-email-code")
    fun sendEmailCode(
        @RequestBody request : SendEmailRequest
    ): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.CREATED).body(emailService.sendEmail(request.email))
    }

}