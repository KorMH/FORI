package com.foodri.foodreview.like.service.Impl;

import com.foodri.foodreview.exception.CustomException;
import com.foodri.foodreview.exception.ErrorCode;
import com.foodri.foodreview.like.entity.Like;
import com.foodri.foodreview.like.repository.LikeRepository;
import com.foodri.foodreview.like.service.LikeService;
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
public class LikeServiceImpl implements LikeService {

  private final LikeRepository likeRepository;
  private final ReviewRepository reviewRepository;
  private final UserRepository userRepository;
  private final NotificationService notificationService;

  @Override
  public void likeReview(Long userId, Long reviewId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_FOUND));

    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.REVIEW_NOT_FOUND));

    likeRepository.save(Like.create(review, user));

    // 리뷰 작성자에게 알림 전송
    notificationService.sendNotification(review.getUser(), user, "리뷰에 좋아요를 눌렀습니다!");
  }

  @Override
  public void unlikeReview(Long userId, Long reviewId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_FOUND));

    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.REVIEW_NOT_FOUND));

    Like like = likeRepository.findByReviewAndUser(review, user)
        .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.LIKE_NOT_FOUND));

    likeRepository.delete(like);
  }

  @Override
  public long getLikeCount(Long reviewId) {
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.REVIEW_NOT_FOUND));

    return likeRepository.countByReview(review);
  }
}