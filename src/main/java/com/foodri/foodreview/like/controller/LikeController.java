package com.foodri.foodreview.like.controller;


import com.foodri.foodreview.like.service.LikeService;
import com.foodri.foodreview.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class LikeController {

  private final LikeService likeService;

  @PostMapping("/{reviewId}/like")
  public ResponseEntity<Void> likeReview(@AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long reviewId) {
    likeService.likeReview(userDetails.getId(), reviewId);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{reviewId}/like")
  public ResponseEntity<Void> unlikeReview(@AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long reviewId) {
    likeService.unlikeReview(userDetails.getId(), reviewId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{reviewId}/likes")
  public ResponseEntity<Long> getLikeCount(@PathVariable Long reviewId) {
    return ResponseEntity.ok(likeService.getLikeCount(reviewId));
  }
}
