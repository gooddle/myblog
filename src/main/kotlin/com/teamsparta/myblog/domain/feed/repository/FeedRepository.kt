package com.teamsparta.myblog.domain.feed.repository

import com.teamsparta.myblog.domain.feed.model.Feed
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime


interface FeedRepository: JpaRepository<Feed, Long> {

    fun findByDeletedFalse(pageable: Pageable): Page<Feed>

    @Query("SELECT f FROM Feed f WHERE f.deleted = true AND f.deletedAt < :olderFeeds")
    fun findDeletedFeeds(@Param("olderFeeds") olderFeeds: LocalDateTime): List<Long>

    fun deleteByIdIn(deletedFeedIds: List<Long>)

}