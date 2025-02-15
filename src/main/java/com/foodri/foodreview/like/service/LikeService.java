package com.foodri.foodreview.like.service;

public interface LikeService {
  void likeReview(Long userId, Long reviewId);
  void unlikeReview(Long userId, Long reviewId);
  long getLikeCount(Long reviewId);

}
