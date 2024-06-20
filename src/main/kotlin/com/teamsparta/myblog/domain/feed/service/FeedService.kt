package com.teamsparta.myblog.domain.feed.service

import com.teamsparta.blog.domain.feed.dto.FeedResponse
import com.teamsparta.myblog.domain.feed.dto.FeedRequest
import com.teamsparta.myblog.domain.feed.dto.GetFeedResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.Authentication

interface FeedService {

    fun getFeedList(pageable: Pageable): Page<GetFeedResponse>
    fun getFeedById(feedId:Long) : FeedResponse
    fun createFeed(request: FeedRequest, authentication: Authentication): FeedResponse
    fun updateFeed(feedId: Long,request:FeedRequest,authentication: Authentication): GetFeedResponse
    fun deleteFeed(feedId:Long,authentication: Authentication)


}