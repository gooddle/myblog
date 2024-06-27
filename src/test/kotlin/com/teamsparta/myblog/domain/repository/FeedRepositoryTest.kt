package com.teamsparta.myblog.domain.repository

import com.teamsparta.myblog.domain.feed.model.Feed
import com.teamsparta.myblog.domain.feed.model.FeedCategory
import com.teamsparta.myblog.domain.feed.repository.FeedRepository
import com.teamsparta.myblog.domain.user.model.User
import com.teamsparta.myblog.domain.user.repository.UserRepository
import com.teamsparta.myblog.infra.querydsl.QueryDslSupport
import com.teamsparta.myblog.infra.security.jwt.JwtPlugin
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(value = [QueryDslSupport::class])
@ActiveProfiles("test")
class FeedRepositoryTest {
    fun testFeedList(){
    val user = User(userName = "string1", password = "Abcde12!@")
    userRepository.save(user)
    val feedList= listOf(
        Feed(
            title = "string1",
            content = "string1",
            user = user,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            deleted = false,
            deletedAt = null,
            feedCategory = FeedCategory.IOS,
            comments = mutableListOf()
        ),
        Feed(
            title = "string2",
            content = "string2",
            user = user,
            createdAt = LocalDateTime.now().minusDays(4),
            updatedAt = LocalDateTime.now(),
            deleted = false,
            deletedAt = null,
            feedCategory = FeedCategory.IOS,
            comments = mutableListOf()
        ),
        Feed(
            title = "string3",
            content = "string3",
            user = user,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            deleted = false,
            deletedAt = null,
            feedCategory = FeedCategory.ANDROID,
            comments = mutableListOf()
        ),
        Feed(
            title = "string4",
            content = "string4",
            user = user,
            createdAt = LocalDateTime.now().minusDays(5),
            updatedAt = null,
            deleted = false,
            deletedAt = null,
            feedCategory = FeedCategory.NORMAL,
            comments = mutableListOf()
        ),
        Feed(
            title = "string5",
            content = "string5",
            user = user,
            createdAt = LocalDateTime.now(),
            updatedAt = null,
            deleted = true,
            deletedAt = LocalDateTime.now().minusDays(5),
            feedCategory = FeedCategory.NORMAL,
            comments = mutableListOf()
        )
    )
        feedRepository.saveAllAndFlush(feedList)
}



    @Autowired
    lateinit var feedRepository: FeedRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Test
    fun`feed list filter 조건 없을때`(){
    //GIVEN
    testFeedList()
    val page = PageRequest.of(0,5)
    //WHEN
    val result1 = feedRepository.findByDeletedFalse(page,null,null,null,null)
    //THEN
    result1.content.size shouldBe 4


    }

    @Test
    fun `feed list filter 제목을 통한 조회`(){
        //GIVEN
        testFeedList()
        val page = PageRequest.of(0,5)

        //WHEN
        val result1 = feedRepository.findByDeletedFalse(page,"1",null,null,null)
        val result2 = feedRepository.findByDeletedFalse(page,"string",null,null,null)

        //THEN
        result1.content.size shouldBe 1
        result2.content.size shouldBe 4

    }

    @Test
    fun `feed list filter 날짜 통한 조회`(){
        //GIVEN
        testFeedList()
        val page = PageRequest.of(0,5)

        //WHEN
        val result1 = feedRepository.findByDeletedFalse(page,null,6,4,null)

        //THEN
        result1.content.size shouldBe 2

    }

    @Test
    fun `feed list filter 카레고리를 통한 조회`(){
        //GIVEN
        testFeedList()
        val page = PageRequest.of(0,5)

        //WHEN
        val result1 = feedRepository.findByDeletedFalse(page,null,null,null,FeedCategory.IOS)

        //THEN
        result1.content.size shouldBe 2
    }

    @Test
    fun`scheduler 기능 확인`(){
        //GIVEN
        testFeedList()
        val olderFeed = LocalDateTime.now().minusHours(12)

        //WHEN
        val result1 = feedRepository.findAndDeleteByDeletedAtBefore(olderFeed)

        //THEN
        result1.size shouldBe 1

    }

    @Test
    fun `feed list filter 복합적인 사용 조회`(){
        //GIVEN
        testFeedList()
        val page = PageRequest.of(0,5)

        //WHEN
        val result1 = feedRepository.findByDeletedFalse(page,null,6,3,FeedCategory.IOS)
        val result2 = feedRepository.findByDeletedFalse(page,"1",null,null,FeedCategory.IOS)

        //THEN
        result1.content.size shouldBe 1
        result2.content.size shouldBe 1
    }




}

