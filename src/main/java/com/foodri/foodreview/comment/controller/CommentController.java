package com.foodri.foodreview.comment.controller;

import com.foodri.foodreview.comment.dto.CommentDto;
import com.foodri.foodreview.comment.dto.CommentRequestDto;
import com.foodri.foodreview.comment.dto.CommentUpdateRequestDto;
import com.foodri.foodreview.comment.service.CommentService;
import com.foodri.foodreview.comment.service.Impl.CommentServiceImpl;
import com.foodri.foodreview.user.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/{reviewId}/comments")
  public ResponseEntity<CommentDto> addComment(@AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long reviewId,
      @RequestBody @Valid CommentRequestDto request) {
    return ResponseEntity.ok(commentService.addComment(userDetails.getId(), reviewId, request.getContent()));
  }

  // 댓글 수정
  @PutMapping("/comments/{commentId}")
  public ResponseEntity<CommentDto> updateComment(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long commentId,
      @RequestBody @Valid CommentUpdateRequestDto request) {
    return ResponseEntity.ok(commentService.updateComment(userDetails.getId(), commentId, request.getContent()));
  }

  // 댓글 삭제
  @DeleteMapping("/comments/{commentId}")
  public ResponseEntity<Void> deleteComment(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long commentId) {
    commentService.deleteComment(userDetails.getId(), commentId);
    return ResponseEntity.noContent().build();
  }
}