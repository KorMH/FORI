package com.foodri.foodreview.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter

@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {
  @NotBlank
  private String content;
  @Min(1) @Max(5)
  private int rating;
  private String imageUrl;
  @NotNull
  private Long receiptId;
}