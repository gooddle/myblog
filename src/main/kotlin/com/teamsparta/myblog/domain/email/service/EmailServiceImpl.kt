package com.teamsparta.myblog.domain.email.service

import com.teamsparta.myblog.infra.RedisUtils
import jakarta.mail.Message
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import org.springframework.mail.MailSendException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import org.springframework.mail.MailException
import kotlin.random.Random


@Service
class EmailServiceImpl(
    private val javaMailSender: JavaMailSender,
    private val redisUtils: RedisUtils,
) : EmailService {

    //메일 발송 시
    override fun sendEmail(email: String):Boolean {
        val codeNumber = generateCodeNumber()
        redisUtils.setDataExpire(email, codeNumber)

        val message = getMailMessage(email, codeNumber)

        try {
            javaMailSender.send(message)
        } catch (e: MailException) {
            throw MailSendException("메일 전송 중 오류가 발생했습니다.", e)
        }
        return true
    }

    //랜덤하게 인증 코드 생성 중복된 숫자 가능
    private fun generateCodeNumber(): String {
        return Random.nextInt(10000,99999).toString()
    }


    //메일로 인증 코드 받을 때  메세지
    private fun getMailMessage(email: String, codeNumber: String): MimeMessage {
        val message = javaMailSender.createMimeMessage()
        message.addRecipient(Message.RecipientType.TO, InternetAddress(email))
        message.subject = " 본인 인증 메일"
        message.setText(getText(codeNumber))
        return message
    }

    //인증 코드 받을 때 세부 메세지
    private fun getText(codeNumber: String): String {
        return "인증번호는 $codeNumber 입니다."
    }

}

