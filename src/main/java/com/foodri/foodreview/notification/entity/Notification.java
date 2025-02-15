package com.foodri.foodreview.notification.entity;

import com.foodri.foodreview.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notifications")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "receiver_id", nullable = false)
  private User receiver;  // 알림을 받는 사용자

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sender_id", nullable = false)
  private User sender;  // 알림을 보내는 사용자

  @Column(nullable = false)
  private String message;

  @Column(nullable = false)
  private boolean isRead;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  public static Notification create(User receiver, User sender, String message) {
    return Notification.builder()
        .receiver(receiver)
        .sender(sender)
        .message(message)
        .isRead(false)
        .createdAt(LocalDateTime.now())
        .build();
  }

  public void markAsRead() {
    this.isRead = true;
  }
}