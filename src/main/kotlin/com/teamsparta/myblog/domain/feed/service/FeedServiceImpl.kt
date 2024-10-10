package com.teamsparta.myblog.domain.feed.service

import com.teamsparta.myblog.domain.exception.ModelNotFoundException
import com.teamsparta.myblog.domain.feed.dto.CreateFeedResponse
import com.teamsparta.myblog.domain.feed.dto.FeedRequest
import com.teamsparta.myblog.domain.feed.dto.PageFeedResponse
import com.teamsparta.myblog.domain.feed.dto.UpdateFeedResponse
import com.teamsparta.myblog.domain.feed.model.Feed
import com.teamsparta.myblog.domain.feed.model.FeedCategory
import com.teamsparta.myblog.domain.feed.model.toResponse
import com.teamsparta.myblog.domain.feed.model.toUpdateResponse
import com.teamsparta.myblog.domain.feed.repository.FeedRepository
import com.teamsparta.myblog.domain.user.model.User
import com.teamsparta.myblog.domain.user.repository.UserRepository
import com.teamsparta.myblog.infra.security.UserPrincipal
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
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

    override fun getFeedList(pageable: Pageable,title: String?,firstDay: Long?,secondDay: Long?,category: FeedCategory?): Page<PageFeedResponse> {
        val page = PageRequest.of(pageable.pageNumber, 5)
        val feeds = feedRepository.findByDeletedFalse(page,title,firstDay,secondDay,category)
        return feeds.map { feed -> PageFeedResponse.from(feed) }
    }

    override fun getFeedById(feedId: Long): UpdateFeedResponse {
        val feed = feedRepository.findByFeedIdWithComments(feedId) ?: throw ModelNotFoundException("Feed")
        if(feed.deleted) throw ModelNotFoundException("feed")
        return feed.toUpdateResponse()
    }

    @Transactional
    override fun createFeed(request: FeedRequest, category: FeedCategory,authentication: Authentication): CreateFeedResponse {
        val user = findUserByAuthentication(authentication)
        val feed = Feed(
            title = request.title,
            content = request.content,
            user = user,
           feedCategory = category,
        )
        return feedRepository.save(feed).toResponse()
    }

    @Transactional
    override fun updateFeed(feedId: Long, request: FeedRequest,category: FeedCategory, authentication: Authentication): UpdateFeedResponse {
        val user = findUserByAuthentication(authentication)
        val feed = findFeedById(feedId)
        checkUserAuthorization(user, feed)

        if (feed.deleted) throw ModelNotFoundException("Feed")
        feed.feedCategory = category
        feed.createFeedRequest(request)
        return feed.toUpdateResponse()
    }

    @Transactional
    override fun deleteFeed(feedId: Long, authentication: Authentication) {
        val user = findUserByAuthentication(authentication)
        val feed = findFeedById(feedId)
        checkUserAuthorization(user, feed)

        if (feed.deleted) throw ModelNotFoundException("Feed")

        feed.softDeleted()
    }

    @Transactional
    override fun recoverFeed(feedId: Long,authentication: Authentication): UpdateFeedResponse {
        val user =findUserByAuthentication(authentication)
        val feed =findFeedById(feedId)
        checkUserAuthorization(user, feed)

        if (feed.deleted) feed.deleted =false
        else throw ModelNotFoundException("feed")

        feed.status()
        return feed.toUpdateResponse()
    }




    private fun findUserByAuthentication(authentication: Authentication): User {
        val userPrincipal = authentication.principal as UserPrincipal
        return userRepository.findByEmail(userPrincipal.email) ?: throw ModelNotFoundException("User")
    }

    private fun findFeedById(feedId: Long): Feed {
        return feedRepository.findByIdOrNull(feedId) ?: throw ModelNotFoundException("Feed")
    }

    private fun checkUserAuthorization(user: User, feed: Feed) {
        if (user.id != feed.user.id) throw IllegalStateException("권한이 없습니다.")
    }
}

