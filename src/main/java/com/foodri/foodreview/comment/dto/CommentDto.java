package com.foodri.foodreview.comment.dto;

import com.foodri.foodreview.comment.entity.Comment;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {

  private Long id;
  private String content;
  private Long userId;
  private String userEmail;
  private Long reviewId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static CommentDto fromEntity(Comment comment) {
    return CommentDto.builder()
        .id(comment.getId())
        .content(comment.getContent())
        .userId(comment.getUser().getId())
        .userEmail(comment.getUser().getEmail())
        .reviewId(comment.getReview().getId())
        .createdAt(comment.getCreatedAt())
        .updatedAt(comment.getUpdatedAt())
        .build();
  }
}

