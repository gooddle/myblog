package com.teamsparta.myblog.domain.feed


import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.teamsparta.myblog.domain.feed.dto.ApiFeedResponse
import com.teamsparta.myblog.domain.feed.dto.UpdateFeedResponse
import com.teamsparta.myblog.domain.feed.service.FeedService
import com.teamsparta.myblog.infra.querydsl.QueryDslSupport
import com.teamsparta.myblog.infra.security.jwt.JwtPlugin
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import java.time.LocalDateTime



@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(value = [QueryDslSupport::class])
@ExtendWith(MockKExtension::class)
class FeedControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val jwtPlugin: JwtPlugin,
    private val objectMapper: ObjectMapper
) : DescribeSpec({

    extension(SpringExtension)

    afterContainer {
        clearAllMocks()
    }

    val feedService = mockk<FeedService>()

    fun testFeedList():List<UpdateFeedResponse> {
        return listOf(
            UpdateFeedResponse(
                id = 1L,
                title = "string1",
                content = "string1",
                createdAt = LocalDateTime.now(),
                updatedAt = null,
                deleted = false,
                comments = mutableListOf(),
                category = "IOS"
            ),

            UpdateFeedResponse(
                id = 2L,
                title = "string2",
                content = "string2",
                createdAt = LocalDateTime.now(),
                updatedAt = null,
                deleted = false,
                comments = mutableListOf(),
                category = "IOS"
            ),

            UpdateFeedResponse(
                id = 3L,
                title = "string3",
                content = "string3",
                createdAt = LocalDateTime.now(),
                updatedAt = null,
                deleted = false,
                comments = mutableListOf(),
                category = "IOS"
            ),

            UpdateFeedResponse(
                id = 4L,
                title = "string4",
                content = "string4",
                createdAt = LocalDateTime.now(),
                updatedAt = null,
                deleted = false,
                comments = mutableListOf(),
                category = "IOS"
            ),

            UpdateFeedResponse(
                id = 5L,
                title = "string5",
                content = "string5",
                createdAt = LocalDateTime.now(),
                updatedAt = null,
                deleted = false,
                comments = mutableListOf(),
                category = "IOS"
            )
        )
    }


    describe("GET /api/v1/feeds/{feedId}") {
        context("존재하는 Id 요청 보낼 때") {
            it("200 status code 응답") {
                val feedId = 30L

                val feedResponse = ApiFeedResponse.success(
                    message = "Success",
                    status = HttpStatus.OK.value(),
                    data = UpdateFeedResponse(
                        id = feedId,
                        title = "string",
                        content = "string",
                        createdAt = LocalDateTime.now(),
                        updatedAt = null,
                        deleted = false,
                        category = "NORMAL",
                        comments = mutableListOf()
                    )
                )

                every { feedService.getFeedById(any()) } returns feedResponse.data!!

                val jwtToken = jwtPlugin.generateAccessToken(
                    subject = feedId.toString(),
                    userName = "string123",
                )

                val result = mockMvc.perform(
                    get("/api/v1/feeds/$feedId")
                        .header("Authorization", "Bearer $jwtToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andReturn()

                result.response.status shouldBe 200


                // 스프링에서 objectMapper 지원해준다
                // 재네릭인 경우 TypeReference 사용해서 명확하게 표현

                val responseDto = objectMapper.readValue(
                    result.response.contentAsString,
                    object : TypeReference<ApiFeedResponse<UpdateFeedResponse>>() {}
                )

                responseDto.data!!.id shouldBe feedId
            }
        }
    }

    describe("GET/api/v1/feeds"){
        context("전체 feed 조회"){
            it("200 status code 반환 "){
                val userId = 1L

                val page = PageRequest.of(0,5)

                val jwtToken = jwtPlugin.generateAccessToken(
                    subject = userId.toString(),
                    userName = "string123",
                )

                val feedList =testFeedList()
                val pageImpl = PageImpl(feedList, page, feedList.size.toLong())

                every { feedService.getFeedList(any(),any(),any(),any(),any()) } returns pageImpl

                val result = mockMvc.perform(
                    get("/api/v1/feeds")
                        .header("Authorization", "Bearer $jwtToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andReturn()

                //THEN
                result.response.status shouldBe 200
            }

        }
    }

})














































