package com.teamsparta.blog.domain.feed.dto


import com.teamsparta.myblog.domain.comment.dto.CreateCommentResponse
import java.time.LocalDateTime

data class CreateFeedResponse(
    val id :Long,
    val title : String,
    val content : String,
    val createdAt : LocalDateTime,
    val isDeleted: Boolean,
    val comments :List<CreateCommentResponse>
)