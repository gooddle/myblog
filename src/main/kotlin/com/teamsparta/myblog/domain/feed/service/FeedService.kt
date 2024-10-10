package com.teamsparta.myblog.domain.feed.service

import com.teamsparta.myblog.domain.feed.dto.CreateFeedResponse
import com.teamsparta.myblog.domain.feed.dto.FeedRequest
import com.teamsparta.myblog.domain.feed.dto.PageFeedResponse
import com.teamsparta.myblog.domain.feed.dto.UpdateFeedResponse
import com.teamsparta.myblog.domain.feed.model.FeedCategory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.Authentication

interface FeedService {

    fun getFeedList(pageable: Pageable,title: String?,firstDay: Long?,secondDay: Long?,category: FeedCategory?): Page<PageFeedResponse>
    fun getFeedById(feedId:Long) : UpdateFeedResponse
    fun createFeed(request: FeedRequest, category: FeedCategory,authentication: Authentication): CreateFeedResponse
    fun updateFeed(feedId: Long,request:FeedRequest,category: FeedCategory,authentication: Authentication): UpdateFeedResponse
    fun deleteFeed(feedId:Long,authentication: Authentication)
    fun recoverFeed(feedId: Long,authentication: Authentication) :UpdateFeedResponse
}