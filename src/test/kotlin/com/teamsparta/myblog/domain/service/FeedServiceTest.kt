package com.teamsparta.myblog.domain.service


import com.teamsparta.myblog.domain.feed.repository.FeedRepository
import com.teamsparta.myblog.domain.feed.service.FeedServiceImpl
import com.teamsparta.myblog.domain.user.repository.UserRepository
import com.teamsparta.myblog.infra.aop.NotFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk


class FeedServiceTest : BehaviorSpec({

    val feedRepository = mockk<FeedRepository>()
    val userRepository = mockk<UserRepository>()
    val feedService = FeedServiceImpl(feedRepository, userRepository)



    Given("특정 feed가 true인 상태 ") {
        val feedId = 1L

        When("특정 feed를 조회하면") {
            every { feedRepository.findByFeedIdWithComments(feedId) } returns null

            Then("feed의 정보가 반환되어야 한다.") {
             shouldThrow<NotFoundException> {
                 feedService.getFeedById(feedId)
             }
            }
        }
    }
})
