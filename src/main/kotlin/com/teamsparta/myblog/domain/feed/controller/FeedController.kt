package com.teamsparta.myblog.domain.feed.controller

import com.teamsparta.myblog.domain.feed.dto.*
import com.teamsparta.myblog.domain.feed.model.FeedCategory
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
    fun getFeedList(@PageableDefault pageable: Pageable
                   ,@RequestParam(required = false)title: String?,firstDay: Long?,secondDay: Long?,category: FeedCategory?
    ): ResponseEntity<Page<UpdateFeedResponse>> {

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(feedService.getFeedList(pageable,title,firstDay,secondDay,category))
    }

    @GetMapping("{feedId}")
    fun getFeedById(@PathVariable feedId: Long): ResponseEntity<ApiFeedResponse<UpdateFeedResponse>> {
       return try{
           val getFeedById = feedService.getFeedById(feedId)
           val response = ApiFeedResponse.success("${feedId}번 게시글 조회",HttpStatus.OK.value(),getFeedById)
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
            val response = ApiFeedResponse.success("게시글 작성 성공",HttpStatus.CREATED.value(), createFeed)
            ResponseEntity.status(HttpStatus.CREATED).body(response)
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
        ): ResponseEntity<ApiFeedResponse<UpdateFeedResponse>> {
            return try {
                feedService.updateFeed(feedId,request,authentication)
                ResponseEntity.status(HttpStatus.OK).body(ApiFeedResponse.success("${feedId}번 게시글 수정완료",HttpStatus.OK.value(),null))

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
                feedService.deleteFeed(feedId,authentication)
                ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiFeedResponse.success("${feedId}번 게시글 삭제 성공",HttpStatus.NO_CONTENT.value(),null))



            } catch (e: NotFoundException) {
                ResponseEntity.badRequest().body(ApiFeedResponse.error(e.message))
            }

        }

        @PutMapping("/recover/{feedId}")
        fun recoverFeed(
            @PathVariable feedId: Long,
            authentication: Authentication
        ): ResponseEntity<ApiFeedResponse<UpdateFeedResponse>> {
            return try {
                feedService.recoverFeed(feedId,authentication)
                ResponseEntity.status(HttpStatus.OK).body(ApiFeedResponse.success("${feedId}번 게시물 복구,",HttpStatus.OK.value(),null))

            } catch (e: NotFoundException) {
                ResponseEntity.badRequest().body(ApiFeedResponse.error(e.message))
            }
        }
}
