package com.teamsparta.myblog.domain.comment.dto

import com.teamsparta.myblog.domain.comment.model.Comment
import java.time.LocalDateTime

data class UpdateCommentResponse(
    val id :Long,
    val title :String,
    val content :String,
    val createdAt : LocalDateTime,
    val updatedAt :LocalDateTime?
){
    companion object{
        fun from(comment: Comment) : UpdateCommentResponse{
            return UpdateCommentResponse(
                id =comment.id!!,
                title = comment.title,
                content = comment.content,
                createdAt = comment.createdAt,
                updatedAt = comment.updatedAt!!
            )
        }
    }
}