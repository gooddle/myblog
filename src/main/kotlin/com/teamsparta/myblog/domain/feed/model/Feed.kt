package com.teamsparta.myblog.domain.feed.model

import com.teamsparta.blog.domain.feed.dto.FeedResponse
import com.teamsparta.myblog.domain.comment.model.Comment
import com.teamsparta.myblog.domain.comment.model.toResponse
import com.teamsparta.myblog.domain.feed.dto.FeedRequest
import com.teamsparta.myblog.domain.user.model.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "feed")
class Feed(

    @Column(name ="title",nullable = false)
    var title: String,

    @Column(name = "content",nullable = false)
    var content: String,

    @Column(name ="created_at",nullable=false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name ="updated_at",nullable = false)
    var updatedAt: LocalDateTime?= LocalDateTime.now(),

    @Column(name ="is_deleted", nullable = false)
    var isDeleted: Boolean = false,

    @Column(name ="deleted_at",nullable=false)
    var deletedAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "feed", cascade = [(CascadeType.ALL)],orphanRemoval = true,fetch = FetchType.LAZY)
    var comments: MutableList<Comment> = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id")
    var user : User


) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null


    fun createFeedRequest(request : FeedRequest) {
        title =request.title
        content = request.content
        updatedAt = LocalDateTime.now()
    }

    fun softDeleted(){
        isDeleted = true
        deletedAt = LocalDateTime.now()
    }



}

fun Feed.toResponse(): FeedResponse {
    return FeedResponse(
        id =id!!,
        title = title,
        content = content,
        createdAt = createdAt,
        isDeleted = isDeleted,
        comments = comments.map { it.toResponse() }
    )
}