package com.teamsparta.myblog.domain.feed.dto


import com.teamsparta.myblog.domain.comment.dto.GetCommentResponse
import com.teamsparta.myblog.domain.comment.model.toUpdateResponse
import com.teamsparta.myblog.domain.feed.model.Feed
import org.springframework.data.domain.Page
import java.time.LocalDateTime

data class GetFeedResponse(
    val id :Long,
    val title : String,
    val content : String,
    val createdAt : LocalDateTime,
    val updatedAt  : LocalDateTime?,
    val deleted: Boolean,
    val comments :List<GetCommentResponse>
){
    companion object {
        fun from(feed: Feed): GetFeedResponse {
            return GetFeedResponse(
                feed.id!!,
                feed.title,
                feed.content,
                feed.createdAt,
                feed.updatedAt,
                feed.deleted,
                feed.comments.map { it.toUpdateResponse() }
            )
        }
        fun from(feeds: Page<Feed>): Page<GetFeedResponse> {
            return feeds.map { from(it) }
        }
    }
}