package com.teamsparta.myblog.domain.comment.controller

import com.teamsparta.myblog.domain.comment.dto.ApiCommentResponse
import com.teamsparta.myblog.domain.comment.dto.CommentRequest
import com.teamsparta.myblog.domain.comment.dto.CreateCommentResponse
import com.teamsparta.myblog.domain.comment.dto.GetCommentResponse
import com.teamsparta.myblog.domain.comment.service.CommentService
import com.teamsparta.myblog.infra.aop.NotFoundException
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
    ): ResponseEntity<ApiCommentResponse<CreateCommentResponse>> {
        return try {
            val createComment = commentService.createCommentAtFeed(feedId,request,authentication)
            val response = ApiCommentResponse.success("${feedId}번 게시글에 댓글 생성",createComment)
            ResponseEntity.status(HttpStatus.CREATED).body(response)
        }
        catch (e:NotFoundException){
            ResponseEntity.badRequest().body(ApiCommentResponse.error(e.message))
        }
    }

    @PutMapping("/{commentId}")
    fun updateComment(@PathVariable feedId: Long,
                      @PathVariable commentId: Long,
                      @RequestBody request: CommentRequest,
                      authentication: Authentication
    ): ResponseEntity<ApiCommentResponse<GetCommentResponse>> {
        return try {
            val updateComment =commentService.updateCommentAtFeed(feedId,commentId,request,authentication)
            val response = ApiCommentResponse.success("${feedId}번 피드에 ${commentId}번 댓글 수정",updateComment)
            ResponseEntity.status(HttpStatus.OK).body(response)
        }
        catch (e:NotFoundException){
            ResponseEntity.badRequest().body(ApiCommentResponse.error(e.message))
        }
    }


    @DeleteMapping("/{commentId}")
    fun deleteComment(@PathVariable feedId: Long,
                      @PathVariable commentId: Long,
                      authentication: Authentication
    ): ResponseEntity<ApiCommentResponse<Unit>> {
        return try {
            val deleteComment =commentService.deleteCommentAtFeed(feedId,commentId,authentication)
            val response = ApiCommentResponse.success("${feedId}번 게시글 ${commentId}번 댓글 삭제 완료",deleteComment)
            ResponseEntity.status(HttpStatus.NO_CONTENT).body(response)
        }
        catch (e:NotFoundException){
            ResponseEntity.badRequest().body(ApiCommentResponse.error(e.message))
        }
    }



}