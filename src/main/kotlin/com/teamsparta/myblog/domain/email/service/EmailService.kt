package com.teamsparta.myblog.domain.email.service




interface EmailService {
    fun sendEmail(email :String):Boolean

}