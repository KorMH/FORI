package com.foodri.foodreview.comment.service.Impl;

import com.foodri.foodreview.comment.dto.CommentDto;
import com.foodri.foodreview.comment.entity.Comment;
import com.foodri.foodreview.comment.repository.CommentRepository;
import com.foodri.foodreview.comment.service.CommentService;
import com.foodri.foodreview.exception.CustomException;
import com.foodri.foodreview.exception.ErrorCode;
import com.foodri.foodreview.notification.service.NotificationService;
import com.foodri.foodreview.review.entity.Review;
import com.foodri.foodreview.review.repository.ReviewRepository;
import com.foodri.foodreview.user.entity.User;
import com.foodri.foodreview.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;
  private final ReviewRepository reviewRepository;
  private final UserRepository userRepository;
  private final NotificationService notificationService;
  
  //댓글 생성
  @Override
  public CommentDto addComment(Long userId, Long reviewId, String content) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_FOUND));

    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.REVIEW_NOT_FOUND));

    Comment comment = commentRepository.save(Comment.create(review, user, content));

    // 리뷰 작성자에게 알림 전송
    notificationService.sendNotification(review.getUser(), user, "리뷰에 댓글을 달았습니다!");

    return CommentDto.fromEntity(comment);
  }


  // 댓글 수정
  @Override
  public CommentDto updateComment(Long userId, Long commentId, String content) {
    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.COMMENT_NOT_FOUND));

    if (!comment.getUser().getId().equals(userId)) {
      throw new CustomException(HttpStatus.FORBIDDEN, ErrorCode.UNAUTHORIZED);
    }

    comment.update(content); // 엔티티에서 update 메서드 호출

    return CommentDto.fromEntity(comment);
  }

  // 댓글 삭제
  @Override
  public void deleteComment(Long userId, Long commentId) {
    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.COMMENT_NOT_FOUND));

    if (!comment.getUser().getId().equals(userId)) {
      throw new CustomException(HttpStatus.FORBIDDEN, ErrorCode.UNAUTHORIZED);
    }

    commentRepository.delete(comment);
  }
}
