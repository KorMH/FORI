package com.foodri.foodreview.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateRequestDto {

  @NotBlank(message = "댓글 내용은 비워둘 수 없습니다.")
  private String content;
}
