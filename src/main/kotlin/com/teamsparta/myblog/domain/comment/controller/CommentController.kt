package com.teamsparta.myblog.domain.comment.controller

import com.teamsparta.myblog.domain.comment.dto.CommentRequest
import com.teamsparta.myblog.domain.comment.dto.CreateCommentResponse
import com.teamsparta.myblog.domain.comment.dto.UpdateCommentResponse
import com.teamsparta.myblog.domain.comment.service.CommentService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/v1/feed/{feedId}/comments")
@RestController
class CommentController(
    val commentService: CommentService,
) {

    @PostMapping
    fun createComment(@PathVariable feedId:Long,
                      @RequestBody request: CommentRequest,
                      authentication: Authentication
    ): ResponseEntity<CreateCommentResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(commentService.createCommentAtFeed(feedId,request,authentication))
    }

    @PutMapping("/{commentId}")
    fun updateComment(@PathVariable feedId: Long,
                      @PathVariable commentId: Long,
                      @RequestBody request: CommentRequest,
                      authentication: Authentication
    ): ResponseEntity<UpdateCommentResponse>{
        return ResponseEntity.status(HttpStatus.OK)
            .body(commentService.updateCommentAtFeed(feedId,commentId,request,authentication))
    }


    @DeleteMapping("/{commentId}")
    fun deleteComment(@PathVariable feedId: Long,
                      @PathVariable commentId: Long,
                      authentication: Authentication
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(commentService.deleteCommentAtFeed(feedId, commentId, authentication))

    }



}