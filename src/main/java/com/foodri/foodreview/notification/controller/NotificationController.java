package com.foodri.foodreview.notification.controller;

import com.foodri.foodreview.config.sse.SseEmitters;
import com.foodri.foodreview.notification.dto.NotificationDto;
import com.foodri.foodreview.notification.service.NotificationService;
import com.foodri.foodreview.user.dto.CustomUserDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;
  private final SseEmitters sseEmitters;

  @GetMapping("/subscribe")
  public SseEmitter subscribe(@AuthenticationPrincipal CustomUserDetails userDetails) {
    return sseEmitters.addEmitter(userDetails.getId());
  }

  @GetMapping
  public ResponseEntity<List<NotificationDto>> getNotifications(@AuthenticationPrincipal CustomUserDetails userDetails) {
    return ResponseEntity.ok(notificationService.getUnreadNotifications(userDetails.getId()));
  }

  @PostMapping("/read")
  public ResponseEntity<Void> markAsRead(@AuthenticationPrincipal CustomUserDetails userDetails) {
    notificationService.markNotificationsAsRead(userDetails.getId());
    return ResponseEntity.ok().build();
  }
}