package com.teamsparta.myblog.domain.user.dto

import org.springframework.http.HttpStatus

data class ApiUserResponse<T>(
    var message: String?,
    var status: Int?,
    var data: T?
){
    companion object {
        fun <T> success(message: String,status: Int,data: T?): ApiUserResponse<T> {
            return ApiUserResponse(message,status,data)
        }

        fun <T> error(message: String?): ApiUserResponse<T> {
            return ApiUserResponse(message,HttpStatus.BAD_REQUEST.value(),null)
        }
    }
}