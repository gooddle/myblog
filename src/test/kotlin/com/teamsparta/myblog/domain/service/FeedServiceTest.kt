package com.teamsparta.myblog.domain.service


import com.teamsparta.myblog.domain.exception.ModelNotFoundException
import com.teamsparta.myblog.domain.feed.repository.FeedRepository
import com.teamsparta.myblog.domain.feed.service.FeedServiceImpl
import com.teamsparta.myblog.domain.user.repository.UserRepository
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

