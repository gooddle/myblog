package com.teamsparta.myblog.domain.comment.service

import com.teamsparta.myblog.domain.comment.dto.CommentRequest
import com.teamsparta.myblog.domain.comment.dto.CreateCommentResponse
import com.teamsparta.myblog.domain.comment.dto.GetCommentResponse
import org.springframework.security.core.Authentication

interface CommentService {
    fun createCommentAtFeed(feedId:Long,request: CommentRequest,authentication: Authentication) : CreateCommentResponse
    fun updateCommentAtFeed(feedId:Long,commentId:Long,request: CommentRequest,authentication: Authentication) : GetCommentResponse
    fun deleteCommentAtFeed(feedId:Long,commentId:Long,authentication: Authentication)
}