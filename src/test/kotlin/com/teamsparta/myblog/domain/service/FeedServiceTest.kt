package com.teamsparta.myblog.domain.service


import com.teamsparta.myblog.domain.exception.ModelNotFoundException
import com.teamsparta.myblog.domain.feed.repository.FeedRepository
import com.teamsparta.myblog.domain.feed.service.FeedServiceImpl
import com.teamsparta.myblog.domain.user.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk


class FeedServiceTest:BehaviorSpec({


    val feedRepository = mockk<FeedRepository>()
    val userRepository = mockk<UserRepository>()

    val feedService = FeedServiceImpl(feedRepository, userRepository)



    Given("특정 feed가 삭제되었을때 "){
        When("특정 feed를 조회하면 "){
            Then("ModelNotFoundException 발생해야한다."){
                val feedId = 30L
                every { feedRepository.findByFeedIdWithComments(feedId)!!.deleted } returns true

                shouldThrow<ModelNotFoundException> {
                    feedService.getFeedById(feedId)
                }
            }
        }
    }




})

