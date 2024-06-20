package com.teamsparta.myblog.domain.comment.dto

import java.time.LocalDateTime

data class CreateCommentResponse(
    val id :Long,
    val title :String,
    val content :String,
    val createdAt : LocalDateTime
)