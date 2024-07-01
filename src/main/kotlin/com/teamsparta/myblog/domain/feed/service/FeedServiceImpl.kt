package com.teamsparta.myblog.domain.feed.service

import com.teamsparta.myblog.domain.feed.dto.CreateFeedResponse
import com.teamsparta.myblog.domain.feed.dto.FeedRequest
import com.teamsparta.myblog.domain.feed.dto.UpdateFeedResponse
import com.teamsparta.myblog.domain.feed.model.Feed
import com.teamsparta.myblog.domain.feed.model.FeedCategory
import com.teamsparta.myblog.domain.feed.model.toResponse
import com.teamsparta.myblog.domain.feed.model.toUpdateResponse
import com.teamsparta.myblog.domain.feed.repository.FeedRepository
import com.teamsparta.myblog.domain.user.model.User
import com.teamsparta.myblog.domain.user.repository.UserRepository
import com.teamsparta.myblog.infra.aop.NotFoundException
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

    override fun getFeedList(pageable: Pageable,title: String?,firstDay: Long?,secondDay: Long?,category: FeedCategory?): Page<UpdateFeedResponse> {
        val page = PageRequest.of(pageable.pageNumber, 5)
        val feeds = feedRepository.findByDeletedFalse(page,title,firstDay,secondDay,category)
        return feeds.map { it.toUpdateResponse() }
    }

    override fun getFeedById(feedId: Long): UpdateFeedResponse {
        val feed = feedRepository.findByFeedIdWithComments(feedId) ?: throw NotFoundException("Feed not found")
        if(feed.deleted) throw NotFoundException("삭제된 게시물입니다.")
        return feed.toUpdateResponse()
    }

    @Transactional
    override fun createFeed(request: FeedRequest, authentication: Authentication): CreateFeedResponse {
        val user = findUserByAuthentication(authentication)
        val feed = Feed(
            title = request.title,
            content = request.content,
            user = user,
            feedCategory = when(request.category){
                "IOS" -> FeedCategory.IOS
                "ANDROID" -> FeedCategory.ANDROID
                else -> FeedCategory.NORMAL
            }
        )
        return feedRepository.save(feed).toResponse()
    }

    @Transactional
    override fun updateFeed(feedId: Long, request: FeedRequest, authentication: Authentication): UpdateFeedResponse {
        val user = findUserByAuthentication(authentication)
        val feed = findFeedById(feedId)
        checkUserAuthorization(user, feed)

        if (feed.deleted) throw NotFoundException("Feed is deleted")

        feed.createFeedRequest(request)
        return feed.toUpdateResponse()
    }

    @Transactional
    override fun deleteFeed(feedId: Long, authentication: Authentication) {
        val user = findUserByAuthentication(authentication)
        val feed = findFeedById(feedId)
        checkUserAuthorization(user, feed)

        if (feed.deleted) throw NotFoundException("Feed is deleted")

        feed.softDeleted()
    }

    @Transactional
    override fun recoverFeed(feedId: Long,authentication: Authentication): UpdateFeedResponse {
        val user =findUserByAuthentication(authentication)
        val feed =findFeedById(feedId)
        checkUserAuthorization(user, feed)

        if (feed.deleted) feed.deleted =false
        else throw NotFoundException("삭제된 게시글이 아닙니다.")

        feed.status()
        return feed.toUpdateResponse()
    }




    private fun findUserByAuthentication(authentication: Authentication): User {
        val userPrincipal = authentication.principal as UserPrincipal
        return userRepository.findByUserName(userPrincipal.userName) ?: throw NotFoundException("User not found")
    }

    private fun findFeedById(feedId: Long): Feed {
        return feedRepository.findByIdOrNull(feedId) ?: throw NotFoundException("Feed not found")
    }

    private fun checkUserAuthorization(user: User, feed: Feed) {
        if (user.id != feed.user.id) throw NotFoundException("권한이 없습니다.")
    }
}

