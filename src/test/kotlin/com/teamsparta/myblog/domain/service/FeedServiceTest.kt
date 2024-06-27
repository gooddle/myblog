package com.teamsparta.myblog.domain.service


import com.teamsparta.myblog.domain.feed.repository.FeedRepository
import com.teamsparta.myblog.domain.feed.service.FeedServiceImpl
import com.teamsparta.myblog.domain.user.repository.UserRepository
import com.teamsparta.myblog.infra.aop.NotFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest



@SpringBootTest
@ExtendWith(MockKExtension::class)

class FeedServiceTest:BehaviorSpec({
    extension(SpringExtension)

    afterContainer {
        clearAllMocks()
    }

    val feedRepository = mockk<FeedRepository>()
    val userRepository = mockk<UserRepository>()

    val feedService = FeedServiceImpl(feedRepository, userRepository)

    Given("feed 가 존재 하지않을때"){
        When("특정 feed를 조회하면 "){
            Then("NotFoundException이 발생해야한다."){
                val feedId =1L
                every { feedRepository.findByFeedIdWithComments(any()) } returns null

                shouldThrow<NotFoundException> {
                    feedService.getFeedById(feedId)
                }
            }

        }
    }


    Given("특정 feed가 삭제되었을때 "){
        When("특정 feed를 조회하면 "){
            Then("NotFoundException 발생해야한다."){
                val feedId = 30L
                every { feedRepository.findByFeedIdWithComments(feedId)!!.deleted } returns true

                shouldThrow<NotFoundException> {
                    feedService.getFeedById(feedId)
                }
            }
        }
    }




})

