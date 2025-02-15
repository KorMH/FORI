package com.foodri.foodreview.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {

  @NotBlank(message = "댓글 내용은 비워둘 수 없습니다.")
  private String content;

  @NotNull(message = "리뷰 ID는 필수입니다.")
  private Long reviewId;
}
