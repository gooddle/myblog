package com.teamsparta.myblog.domain.feed.controller

import com.teamsparta.myblog.domain.feed.dto.ApiFeedResponse
import com.teamsparta.myblog.domain.feed.dto.CreateFeedResponse
import com.teamsparta.myblog.domain.feed.dto.FeedRequest
import com.teamsparta.myblog.domain.feed.dto.GetFeedResponse
import com.teamsparta.myblog.domain.feed.service.FeedService
import com.teamsparta.myblog.infra.aop.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RequestMapping("api/v1/feeds")
@RestController
class FeedController(
    private var feedService: FeedService,

) {

    @GetMapping
    fun getFeedList(@PageableDefault pageable: Pageable): ResponseEntity<Page<GetFeedResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(feedService.getFeedList(pageable))
    }

    @GetMapping("{feedId}")
    fun getFeedById(@PathVariable feedId: Long): ResponseEntity<ApiFeedResponse<GetFeedResponse>> {
       return try{
           val getFeedById = feedService.getFeedById(feedId)
           val response = ApiFeedResponse.success("${feedId}번 조회",getFeedById)
           ResponseEntity.status(HttpStatus.OK).body(response)
       }
       catch (e:NotFoundException){
           ResponseEntity.badRequest().body(ApiFeedResponse.error(e.message))

       }
    }

    @PostMapping
    fun createFeed(
        @RequestBody request: FeedRequest,
        authentication: Authentication
    ): ResponseEntity<ApiFeedResponse<CreateFeedResponse>> {
        return try {
            val createFeed = feedService.createFeed(request, authentication)
            val response = ApiFeedResponse.success("게시글 작성 성공", createFeed)
            ResponseEntity.status(HttpStatus.ACCEPTED).body(response)
        }
        catch (e: NotFoundException) {
            ResponseEntity.badRequest().body(ApiFeedResponse.error(e.message))
        }
    }


        @PutMapping("/{feedId}")
        fun updateFeedById(
            @PathVariable feedId: Long,
            @RequestBody request: FeedRequest,
            authentication: Authentication
        ): ResponseEntity<ApiFeedResponse<GetFeedResponse>> {
            return try {
                val updatedFeed = feedService.updateFeed(feedId, request, authentication)
                val response = ApiFeedResponse.success("게시글 수정 성공", updatedFeed)
                ResponseEntity.status(HttpStatus.ACCEPTED).body(response)

            } catch (e: NotFoundException) {
                ResponseEntity.badRequest().body(ApiFeedResponse.error(e.message))
            }
        }

        @DeleteMapping("/{feedId}")
        fun deleteFeedById(
            @PathVariable feedId: Long,
            authentication: Authentication
        ): ResponseEntity<ApiFeedResponse<Unit>> {
            return try {
                val deletedFeed = feedService.deleteFeed(feedId, authentication)
                val response = ApiFeedResponse.success("게시글 삭제 성공", deletedFeed)
                ResponseEntity.status(HttpStatus.ACCEPTED).body(response)

            } catch (e: NotFoundException) {
                ResponseEntity.badRequest().body(ApiFeedResponse.error(e.message))
            }

        }

        @PostMapping("/{feedId}")
        fun recoverFeed(
            @PathVariable feedId: Long,
            authentication: Authentication
        ): ResponseEntity<ApiFeedResponse<GetFeedResponse>> {
            return try {
                val recoverFeed = feedService.recoverFeed(feedId, authentication)
                val response = ApiFeedResponse.success("게시글 복구 성공", recoverFeed)
                ResponseEntity.status(HttpStatus.ACCEPTED).body(response)

            } catch (e: NotFoundException) {
                ResponseEntity.badRequest().body(ApiFeedResponse.error(e.message))
            }
        }

}
