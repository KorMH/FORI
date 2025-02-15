package com.foodri.foodreview.notification.dto;

import com.foodri.foodreview.notification.entity.Notification;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDto {

  private Long id;
  private String message;
  private Long senderId;
  private String senderName;
  private Long receiverId;
  private String receiverName;
  private boolean isRead;
  private LocalDateTime createdAt;

  public static NotificationDto fromEntity(Notification notification) {
    return NotificationDto.builder()
        .id(notification.getId())
        .message(notification.getMessage())
        .senderId(notification.getSender().getId())
        .senderName(notification.getSender().getEmail())
        .receiverId(notification.getReceiver().getId())
        .receiverName(notification.getReceiver().getEmail())
        .isRead(notification.isRead())
        .createdAt(notification.getCreatedAt())
        .build();
  }
}
