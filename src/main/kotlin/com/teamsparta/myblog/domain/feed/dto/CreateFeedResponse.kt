package com.teamsparta.myblog.domain.feed.dto


import com.teamsparta.myblog.domain.comment.dto.CreateCommentResponse
import com.teamsparta.myblog.domain.feed.model.FeedCategory
import java.time.LocalDateTime

data class CreateFeedResponse(
    val id :Long,
    val title : String,
    val content : String,
    val createdAt : LocalDateTime,
    val deleted: Boolean,
    val category : FeedCategory,
    val comments :List<CreateCommentResponse>
)