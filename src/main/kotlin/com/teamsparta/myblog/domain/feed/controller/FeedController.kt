package com.teamsparta.myblog.domain.feed.controller

import com.teamsparta.blog.domain.feed.dto.CreateFeedResponse
import com.teamsparta.myblog.domain.feed.dto.FeedRequest
import com.teamsparta.myblog.domain.feed.dto.GetFeedResponse
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
    private var feedService: FeedService
) {

    @GetMapping
    fun getFeedList(@PageableDefault pageable: Pageable): ResponseEntity<Page<GetFeedResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(feedService.getFeedList(pageable))
    }

    @GetMapping("{feedId}")
    fun getFeedById(@PathVariable feedId :Long): ResponseEntity<GetFeedResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(feedService.getFeedById(feedId))
    }

    @PostMapping
    fun createFeed(@RequestBody request: FeedRequest, authentication: Authentication): ResponseEntity<CreateFeedResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(feedService.createFeed(request,authentication))
    }

    @PutMapping("/{feedId}")
    fun updateFeedById(@PathVariable feedId :Long, @RequestBody request: FeedRequest, authentication: Authentication): ResponseEntity<GetFeedResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(feedService.updateFeed(feedId,request,authentication))
    }

    @DeleteMapping("/{feedId}")
    fun deleteFeedById(@PathVariable feedId :Long, authentication: Authentication): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(feedService.deleteFeed(feedId,authentication))
    }


}