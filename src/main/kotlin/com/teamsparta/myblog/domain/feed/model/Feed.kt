package com.teamsparta.myblog.domain.feed.model

import com.teamsparta.myblog.domain.feed.dto.CreateFeedResponse
import com.teamsparta.myblog.domain.comment.model.Comment
import com.teamsparta.myblog.domain.comment.model.toResponse
import com.teamsparta.myblog.domain.feed.dto.FeedRequest
import com.teamsparta.myblog.domain.feed.dto.UpdateFeedResponse
import com.teamsparta.myblog.domain.user.model.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "feed")
class Feed(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name ="title",nullable = false)
    var title: String,

    @Column(name = "content",nullable = false)
    var content: String,

    @Column(name ="created_at",nullable=false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name ="updated_at")
    var updatedAt: LocalDateTime?= null,

    @Column(name ="is_deleted", nullable = false)
    var deleted: Boolean = false,

    @Column(name ="deleted_at")
    var deletedAt: LocalDateTime? = null,

    @OneToMany(mappedBy = "feed", cascade = [(CascadeType.ALL)],orphanRemoval = true,fetch = FetchType.LAZY)
    var comments: MutableList<Comment> = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id")
    var user : User,

    @Enumerated(EnumType.STRING)
    @Column(name="category",nullable = false)
    var feedCategory: FeedCategory,


) {

    fun createFeedRequest(request : FeedRequest) {
        title =request.title
        content = request.content
        updatedAt = LocalDateTime.now()
    }

    fun softDeleted(){
        deleted = true
        deletedAt = LocalDateTime.now()
    }
    fun status(){
        deletedAt = null
        updatedAt = LocalDateTime.now()
    }


}

fun Feed.toResponse(): CreateFeedResponse {
    return CreateFeedResponse(
        id =id!!,
        title = title,
        content = content,
        createdAt = createdAt,
        deleted = deleted,
        category = feedCategory,
        comments = comments.map { it.toResponse() }
    )
}

fun Feed.toUpdateResponse(): UpdateFeedResponse {
    return UpdateFeedResponse(
        id =id!!,
        title = title,
        content = content,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deleted = deleted,
        category = feedCategory,
    )
}