package com.teamsparta.myblog.domain.feed.repository

import com.teamsparta.myblog.domain.feed.model.Feed
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

interface FeedQueryDslRepository {
    fun findByDeletedFalse(pageable: Pageable): Page<Feed>


    fun findAndDeleteByDeletedAtBefore(olderFeeds: LocalDateTime): List<Feed>


    fun findByFeedIdWithComments(feedId: Long): Feed?
}