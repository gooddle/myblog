package com.teamsparta.myblog.domain.feed.dto


import com.teamsparta.myblog.domain.feed.model.FeedCategory
import java.time.LocalDateTime

data class UpdateFeedResponse(

    val id :Long,
    val title : String,
    val content : String,
    val createdAt : LocalDateTime,
    val updatedAt  : LocalDateTime?,
    val deleted: Boolean,
    val category :FeedCategory,
)