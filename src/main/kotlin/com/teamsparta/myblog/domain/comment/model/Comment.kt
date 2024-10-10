package com.teamsparta.myblog.domain.comment.model

import com.teamsparta.myblog.domain.comment.dto.CommentRequest
import com.teamsparta.myblog.domain.comment.dto.CreateCommentResponse
import com.teamsparta.myblog.domain.comment.dto.UpdateCommentResponse
import com.teamsparta.myblog.domain.feed.model.Feed
import com.teamsparta.myblog.domain.user.model.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "comment")
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "title")
    var title: String,

    @Column(name = "content")
    var content: String,

    @Column(name = "created_at")
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? =null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    var feed : Feed,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user : User,

    ) {


    fun updateCommentRequest(request: CommentRequest) {
        title=request.title
        content=request.content
        updatedAt=LocalDateTime.now()
    }
}


fun Comment.toResponse(): CreateCommentResponse {
    return CreateCommentResponse(
        id=id!!,
        title =title,
        content = content,
        createdAt = createdAt
    )
}

fun Comment.toUpdateResponse(): UpdateCommentResponse {
    return UpdateCommentResponse(
        id=id!!,
        title=title,
        content = content,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}