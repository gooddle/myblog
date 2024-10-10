package com.teamsparta.myblog.domain.feed.controller

import com.teamsparta.myblog.domain.feed.dto.*
import com.teamsparta.myblog.domain.feed.model.FeedCategory
import com.teamsparta.myblog.domain.feed.service.FeedService
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
    fun getFeedList(
        @PageableDefault pageable: Pageable,
        @RequestParam(required = false) title: String?,
        firstDay: Long?,
        secondDay: Long?,
        category: FeedCategory?
    ): ResponseEntity<Page<PageFeedResponse>> {

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(feedService.getFeedList(pageable, title, firstDay, secondDay, category))
    }

    @GetMapping("{feedId}")
    fun getFeedById(@PathVariable feedId: Long): ResponseEntity<UpdateFeedResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(feedService.getFeedById(feedId))
    }

    @PostMapping
    fun createFeed(
        @RequestBody request: FeedRequest,
        category: FeedCategory,
        authentication: Authentication
    ): ResponseEntity<CreateFeedResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(feedService.createFeed(request,category,authentication))
    }


    @PutMapping("/{feedId}")
    fun updateFeedById(
        @PathVariable feedId: Long,
        @RequestBody request: FeedRequest,
        category: FeedCategory,
        authentication: Authentication
    ): ResponseEntity<UpdateFeedResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(feedService.updateFeed(feedId, request,category, authentication))
    }

    @DeleteMapping("/{feedId}")
    fun deleteFeedById(
        @PathVariable feedId: Long,
        authentication: Authentication
    ): ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(feedService.deleteFeed(feedId, authentication))

    }

    @PutMapping("/recover/{feedId}")
    fun recoverFeed(
        @PathVariable feedId: Long,
        authentication: Authentication
    ): ResponseEntity<UpdateFeedResponse> {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(feedService.recoverFeed(feedId, authentication))
    }
}
