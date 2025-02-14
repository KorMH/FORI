package com.foodri.foodreview.review.service;

import com.foodri.foodreview.review.dto.ReviewDto;
import com.foodri.foodreview.review.dto.ReviewRequestDto;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ReviewService {

  ReviewDto createReview(Long userId, ReviewRequestDto request, MultipartFile imageFile);
  ReviewDto updateReview(Long userId, Long reviewId, ReviewRequestDto request);
  void deleteReview(Long userId, Long reviewId);
  ReviewDto getReview(Long reviewId);
  List<ReviewDto> getUserReviews(Long userId);
}
