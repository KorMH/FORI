package com.foodri.foodreview.review.dto;

import com.foodri.foodreview.review.entity.Review;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {
  private Long id;
  private String content;
  private int rating;
  private String imageUrl;
  private boolean verified;
  private Long userId;
  private Long receiptId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static ReviewDto fromEntity(Review review) {
    return ReviewDto.builder()
        .id(review.getId())
        .content(review.getContent())
        .rating(review.getRating())
        .imageUrl(review.getImageUrl())
        .verified(review.isVerified())
        .userId(review.getUser().getId())
        .receiptId(review.getReceipt().getId())
        .createdAt(review.getCreatedAt())
        .updatedAt(review.getUpdatedAt())
        .build();
  }
}