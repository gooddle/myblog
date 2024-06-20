package com.teamsparta.myblog.domain.feed.repository

import com.teamsparta.myblog.domain.feed.model.Feed
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface FeedRepository: JpaRepository<Feed, Long> {

    fun findByIsDeletedFalse(pageable: Pageable): Page<Feed>
}