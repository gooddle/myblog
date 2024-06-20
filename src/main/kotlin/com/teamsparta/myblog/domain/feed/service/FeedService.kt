package com.teamsparta.myblog.domain.feed.service

import com.teamsparta.myblog.domain.feed.dto.CreateFeedResponse
import com.teamsparta.myblog.domain.feed.dto.FeedRequest
import com.teamsparta.myblog.domain.feed.dto.GetFeedResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.Authentication

interface FeedService {

    fun getFeedList(pageable: Pageable): Page<GetFeedResponse>
    fun getFeedById(feedId:Long) : GetFeedResponse
    fun createFeed(request: FeedRequest, authentication: Authentication): CreateFeedResponse
    fun updateFeed(feedId: Long,request:FeedRequest,authentication: Authentication): GetFeedResponse
    fun deleteFeed(feedId:Long,authentication: Authentication)
    fun recoverFeed(feedId: Long,authentication: Authentication) :GetFeedResponse


}