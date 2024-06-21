package com.teamsparta.myblog.domain.feed.dto

import org.springframework.http.HttpStatus

data class ApiFeedResponse<T>(
    val message: String?,
    val status: Int,
    val data: T?
) {
    companion object {
        fun <T> success(message: String, data: T): ApiFeedResponse<T> {
            return ApiFeedResponse(message, HttpStatus.OK.value(), data)
        }

        fun <T> error(message: String?): ApiFeedResponse<T> {
            return ApiFeedResponse(message, HttpStatus.BAD_REQUEST.value(), null)
        }

    }
}







