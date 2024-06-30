package com.teamsparta.myblog.domain.comment.controller

import com.teamsparta.myblog.domain.comment.dto.ApiCommentResponse
import com.teamsparta.myblog.domain.comment.dto.CommentRequest
import com.teamsparta.myblog.domain.comment.dto.CreateCommentResponse
import com.teamsparta.myblog.domain.comment.dto.UpdateCommentResponse
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
            val response = ApiCommentResponse.success("${feedId}번 게시글에 댓글 생성",HttpStatus.CREATED.value(),createComment)
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
    ): ResponseEntity<ApiCommentResponse<UpdateCommentResponse>> {
        return try {
            commentService.updateCommentAtFeed(feedId,commentId,request,authentication)
            ResponseEntity.status(HttpStatus.OK).body(ApiCommentResponse.success("댓글 업데이트 성공 ",HttpStatus.OK.value(),null))
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
            commentService.deleteCommentAtFeed(feedId,commentId,authentication)
            ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiCommentResponse.success("댓글 삭제 성공",HttpStatus.NO_CONTENT.value(),null))
        }
        catch (e:NotFoundException){
            ResponseEntity.badRequest().body(ApiCommentResponse.error(e.message))
        }
    }



}