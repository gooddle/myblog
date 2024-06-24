package com.teamsparta.myblog.domain.comment.dto

import org.springframework.http.HttpStatus

data class ApiCommentResponse<T>(
    val message: String?,
    val status: Int,
    val data: T?
){
    companion object{
        fun <T> success(message: String?,status: Int,data :T?):ApiCommentResponse<T>{
            return ApiCommentResponse(message, status,data)
        }

        fun <T> error(message: String?):ApiCommentResponse<T>{
            return ApiCommentResponse(message, HttpStatus.BAD_REQUEST.value(),null)
        }
    }
}