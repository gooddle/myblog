package com.teamsparta.myblog.domain.feed.service

import com.teamsparta.blog.domain.feed.dto.CreateFeedResponse
import com.teamsparta.myblog.domain.exception.ModelNotFoundException
import com.teamsparta.myblog.domain.feed.dto.FeedRequest
import com.teamsparta.myblog.domain.feed.dto.GetFeedResponse
import com.teamsparta.myblog.domain.feed.model.Feed
import com.teamsparta.myblog.domain.feed.model.toResponse
import com.teamsparta.myblog.domain.feed.repository.FeedRepository
import com.teamsparta.myblog.domain.user.model.User
import com.teamsparta.myblog.domain.user.repository.UserRepository
import com.teamsparta.myblog.infra.security.UserPrincipal
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class FeedServiceImpl(
    private val feedRepository: FeedRepository,
    private val userRepository: UserRepository,
): FeedService {

    override fun getFeedList(pageable: Pageable): Page<GetFeedResponse> {
        val feeds = feedRepository.findByIsDeletedFalse(pageable)
        return GetFeedResponse.from(feeds)
    }

    override fun getFeedById(feedId: Long): GetFeedResponse {
        val feed = findFeedById(feedId)
        if(feed.isDeleted) throw IllegalStateException("삭제된 게시물입니다.")
        return GetFeedResponse.from(feed)
    }

    @Transactional
    override fun createFeed(request: FeedRequest, authentication: Authentication): CreateFeedResponse {
        val user = findUserByAuthentication(authentication)
        val feed = Feed(
            title = request.title,
            content = request.content,
            user = user
        )
        return feedRepository.save(feed).toResponse()
    }

    @Transactional
    override fun updateFeed(feedId: Long, request: FeedRequest, authentication: Authentication): GetFeedResponse {
        val user = findUserByAuthentication(authentication)
        val feed = findFeedById(feedId)
        checkUserAuthorization(user, feed)

        if (feed.isDeleted) throw ModelNotFoundException("Feed is deleted", feedId)

        feed.createFeedRequest(request)
        return GetFeedResponse.from(feed)
    }

    @Transactional
    override fun deleteFeed(feedId: Long, authentication: Authentication) {
        val user = findUserByAuthentication(authentication)
        val feed = findFeedById(feedId)
        checkUserAuthorization(user, feed)

        if (feed.isDeleted) throw ModelNotFoundException("Feed is deleted", feedId)

        feed.softDeleted()
        feedRepository.save(feed)
    }



    private fun findUserByAuthentication(authentication: Authentication): User {
        val userPrincipal = authentication.principal as UserPrincipal
        return userRepository.findByUserName(userPrincipal.userName) ?: throw IllegalStateException("User not found")
    }

    private fun findFeedById(feedId: Long): Feed {
        return feedRepository.findByIdOrNull(feedId) ?: throw ModelNotFoundException("Feed not found", feedId)
    }

    private fun checkUserAuthorization(user: User, feed: Feed) {
        if (user.id != feed.user.id) throw IllegalStateException("권한이 없습니다.")
    }
}

