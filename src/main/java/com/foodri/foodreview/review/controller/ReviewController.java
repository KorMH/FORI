package com.foodri.foodreview.review.controller;

import com.foodri.foodreview.review.dto.ReviewDto;
import com.foodri.foodreview.review.dto.ReviewRequestDto;
import com.foodri.foodreview.review.service.Impl.ReviewServiceImpl;
import com.foodri.foodreview.user.dto.CustomUserDetails;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewServiceImpl reviewService;

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<ReviewDto> createReview(@AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestPart @Valid ReviewRequestDto request,
      @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
    return ResponseEntity.ok(reviewService.createReview(userDetails.getId(), request,imageFile));
  }

  @PutMapping("/{reviewId}")
  public ResponseEntity<ReviewDto> updateReview(@AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long reviewId, @RequestBody @Valid ReviewRequestDto request) {
    return ResponseEntity.ok(reviewService.updateReview(userDetails.getId(), reviewId, request));
  }

  @DeleteMapping("/{reviewId}")
  public ResponseEntity<Void> deleteReview(@AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long reviewId) {
    reviewService.deleteReview(userDetails.getId(), reviewId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{reviewId}")
  public ResponseEntity<ReviewDto> getReview(@PathVariable Long reviewId) {
    return ResponseEntity.ok(reviewService.getReview(reviewId));
  }

  @GetMapping("/my")
  public ResponseEntity<List<ReviewDto>> getUserReviews(@AuthenticationPrincipal CustomUserDetails userDetails) {
    return ResponseEntity.ok(reviewService.getUserReviews(userDetails.getId()));
  }
}
