package com.teamsparta.myblog.domain.feed.dto

import com.teamsparta.myblog.domain.feed.model.Feed
import com.teamsparta.myblog.domain.feed.model.FeedCategory
import java.time.LocalDateTime

data class PageFeedResponse(
    val id :Long,
    val title :String,
    val category: FeedCategory,
    val createdAt: LocalDateTime,
    val deleted: Boolean
){
    companion object {
        fun from(feed : Feed) : PageFeedResponse {
            return PageFeedResponse(
                id =feed.id!!,
                title=feed.title,
                category = feed.feedCategory,
                createdAt = feed.createdAt,
                deleted = false
            )
        }
    }
}