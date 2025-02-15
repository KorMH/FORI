package com.foodri.foodreview.comment.service;

import com.foodri.foodreview.comment.dto.CommentDto;

public interface CommentService {
  CommentDto addComment(Long userId, Long reviewId, String content);
  CommentDto updateComment(Long userId, Long commentId, String content);
  void deleteComment(Long userId, Long commentId);
}
