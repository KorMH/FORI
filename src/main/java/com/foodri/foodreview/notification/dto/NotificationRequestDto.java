package com.foodri.foodreview.notification.dto;

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
public class NotificationRequestDto {

  @NotNull(message = "수신자 ID는 필수입니다.")
  private Long receiverId;

  @NotBlank(message = "알림 메시지는 비워둘 수 없습니다.")
  private String message;
}
