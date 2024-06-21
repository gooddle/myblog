package com.teamsparta.myblog.domain.feed.repository

import com.teamsparta.myblog.domain.feed.model.Feed
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface FeedRepository: JpaRepository<Feed, Long> {

    fun findByDeletedFalse(pageable: Pageable): Page<Feed>


    fun findByDeletedAtBefore(olderFeeds: LocalDateTime): List<Feed>

    @Modifying
    @Query("DELETE FROM Feed f WHERE f IN :feedList")
    fun deleteIn(@Param("feedList") feedList: List<Feed>)


    @Query("SELECT f FROM Feed f LEFT JOIN FETCH f.comments WHERE f.id = :feedId")
    fun findByIdWithComments(feedId: Long): Feed


}



