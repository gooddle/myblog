package com.teamsparta.myblog.infra

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.Properties

//javaMailSender bean 등록
@Configuration
class EmailConfig(
    @Value("\${mail.host}") private val host: String,
    @Value("\${mail.port}") private val port: Int,
    @Value("\${mail.username}") private val username: String,
    @Value("\${mail.password}") private val password: String
) {


    @Bean
    fun getJavaMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = host
        mailSender.port = port

        mailSender.username =  username
        mailSender.password =  password

        val props: Properties = mailSender.javaMailProperties
        props["mail.transport.protocol"] = "smtp"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.debug"] = "true"

        return mailSender
    }
}
