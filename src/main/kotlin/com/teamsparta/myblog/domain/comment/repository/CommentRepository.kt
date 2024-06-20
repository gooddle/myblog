package com.teamsparta.myblog.domain.comment.repository

import com.teamsparta.myblog.domain.comment.model.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository: JpaRepository<Comment, Long> {
    fun findByFeedIdAndId(feedId: Long, commentId: Long): Comment?
}