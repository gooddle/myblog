package com.teamsparta.myblog.domain.feed.repository

import com.teamsparta.myblog.domain.feed.model.Feed
import com.teamsparta.myblog.domain.feed.model.FeedCategory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

interface FeedQueryDslRepository {
    fun findByDeletedFalse(pageable: Pageable,title: String?,firstDay: Long?,secondDay: Long?,category: FeedCategory?): Page<Feed>


    fun findAndDeleteByDeletedAtBefore(olderFeeds: LocalDateTime): List<Feed>


    fun findByFeedIdWithComments(feedId: Long): Feed?

}