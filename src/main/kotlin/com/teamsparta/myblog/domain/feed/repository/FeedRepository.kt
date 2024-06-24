package com.teamsparta.myblog.domain.feed.repository



import com.teamsparta.myblog.domain.feed.model.Feed
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface FeedRepository: JpaRepository<Feed, Long>,FeedQueryDslRepository {


}










