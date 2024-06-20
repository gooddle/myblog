package com.teamsparta.myblog.domain.comment.service

import com.teamsparta.myblog.domain.comment.dto.CommentRequest
import com.teamsparta.myblog.domain.comment.dto.CreateCommentResponse
import com.teamsparta.myblog.domain.comment.dto.GetCommentResponse
import com.teamsparta.myblog.domain.comment.model.Comment
import com.teamsparta.myblog.domain.comment.model.toResponse
import com.teamsparta.myblog.domain.comment.repository.CommentRepository
import com.teamsparta.myblog.domain.exception.ModelNotFoundException
import com.teamsparta.myblog.domain.feed.repository.FeedRepository
import com.teamsparta.myblog.domain.user.model.User
import com.teamsparta.myblog.domain.user.repository.UserRepository
import com.teamsparta.myblog.infra.security.UserPrincipal
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class CommentServiceImpl(
    private val feedRepository: FeedRepository,
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
): CommentService {
    @Transactional
    override fun createCommentAtFeed(feedId: Long, request: CommentRequest,authentication: Authentication): CreateCommentResponse {
       val user = findUserByAuthentication(authentication)
        val feed = feedRepository.findByIdOrNull(feedId) ?: throw ModelNotFoundException("Feed not found",feedId)
        if(feed.deleted) throw IllegalStateException("삭제된 게시글입니다.")
        val comment = Comment(
            title = request.title,
            content = request.content,
            feed = feed,
            user = user
        )
        feed.comments.add(comment)
        commentRepository.save(comment)
        return comment.toResponse()
    }

    @Transactional
    override fun updateCommentAtFeed(feedId: Long,commentId:Long,request: CommentRequest,authentication: Authentication): GetCommentResponse {
        val user = findUserByAuthentication(authentication)
        val comment = findByFeedIdAndCommentId(feedId,commentId)
        checkUserAuthorization(user,comment)

        if(comment.feed.deleted) throw IllegalStateException("삭제된 게시글입니다.")

        comment.updateCommentRequest(request)
        return GetCommentResponse.from(comment)
    }

    @Transactional
    override fun deleteCommentAtFeed(feedId: Long,commentId:Long,authentication: Authentication) {
        val user = findUserByAuthentication(authentication)
        val comment = findByFeedIdAndCommentId(feedId,commentId)
        checkUserAuthorization(user,comment)

        if(comment.feed.deleted) throw IllegalStateException("삭제된 게시글입니다.")

        commentRepository.delete(comment)

    }

    private fun findUserByAuthentication(authentication:Authentication) : User {
        val userPrincipal = authentication.principal as UserPrincipal
        return userRepository.findByUserName(userPrincipal.userName) ?: throw IllegalStateException("User not found")
    }

    private fun findByFeedIdAndCommentId(feedId: Long,commentId: Long) : Comment {
        return commentRepository.findByFeedIdAndId(feedId,commentId) ?: throw ModelNotFoundException("없는 feed입니다.",feedId)
    }

    private fun checkUserAuthorization(user:User,comment:Comment){
        if(user.id != comment.user.id) throw IllegalStateException("권한이 없습니다.")
    }



}