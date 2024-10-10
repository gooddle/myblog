package com.teamsparta.myblog.domain.feed


import com.teamsparta.myblog.domain.comment.model.Comment
import com.teamsparta.myblog.domain.exception.ModelNotFoundException
import com.teamsparta.myblog.domain.feed.dto.FeedRequest
import com.teamsparta.myblog.domain.feed.model.Feed
import com.teamsparta.myblog.domain.feed.model.FeedCategory
import com.teamsparta.myblog.domain.feed.repository.FeedRepository
import com.teamsparta.myblog.domain.feed.service.FeedServiceImpl
import com.teamsparta.myblog.domain.user.model.User
import com.teamsparta.myblog.domain.user.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime


class FeedServiceTest : BehaviorSpec({

    val feedRepository = mockk<FeedRepository>()
    val userRepository = mockk<UserRepository>()
    val feedService = FeedServiceImpl(feedRepository, userRepository)

    Given("특정 feed가 삭제가 되었다면 ") {
        every { feedRepository.findByFeedIdWithComments(FEED_ID) } returns deletedFeed
        When("특정 feed를 조회하면") {
            Then("ModelNotFoundException 발생 .") {
             shouldThrow<ModelNotFoundException> {
                 feedService.getFeedById(FEED_ID)
             }
            }
        }
    }


}){
    companion object{
        private const val FEED_ID =1L
        private const val USER_ID = 1L
        private const val COMMENT_ID = 1L
        private val comments: MutableList<Comment> = mutableListOf()
        private val user =User(
            id = USER_ID,
            email = "test@test.com",
            password = "tesT12!@",
        )
        private val deletedFeed = Feed(
            id = FEED_ID,
            title = "test1",
            content = "test1",
            createdAt = LocalDateTime.of(2021, 1, 1, 1, 1, 0),
            updatedAt = LocalDateTime.of(2021, 1, 2, 1, 1, 0),
            deleted = true,
            deletedAt = null,
            user = user,
            feedCategory = FeedCategory.IOS
        )
        private val comment = Comment(
            id =COMMENT_ID,
            title = "testcomment1",
            content = "testcomment1",
            createdAt = LocalDateTime.of(2021, 1, 1, 1, 1, 0),
            updatedAt = LocalDateTime.of(2021, 1, 2, 1, 1, 0),
            feed = deletedFeed,
            user = user
        )

        private val updateFeedRequest = FeedRequest(
            title ="testtitle",
            content = "testcontent",
        )
    }
}
