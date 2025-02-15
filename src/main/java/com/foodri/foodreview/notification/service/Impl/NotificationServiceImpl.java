package com.foodri.foodreview.notification.service.Impl;

import com.foodri.foodreview.config.sse.SseEmitters;
import com.foodri.foodreview.exception.CustomException;
import com.foodri.foodreview.exception.ErrorCode;
import com.foodri.foodreview.notification.dto.NotificationDto;
import com.foodri.foodreview.notification.entity.Notification;
import com.foodri.foodreview.notification.repository.NotificationRepository;
import com.foodri.foodreview.notification.service.NotificationService;
import com.foodri.foodreview.user.entity.User;
import com.foodri.foodreview.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;
  private final SseEmitters sseEmitters;


  //알림 저장 후 SSE를 통해 실시간 전송
  @Override
  public void sendNotification(User receiver, User sender, String message) {
    Notification notification = Notification.create(receiver, sender, message);
    notificationRepository.save(notification);

    // SSE를 통해 실시간 알림 전송
    sseEmitters.sendNotification(receiver.getId(), message);
  }

  // 읽지 않은 알림 조회
  @Override
  public List<NotificationDto> getUnreadNotifications(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_FOUND));

    return notificationRepository.findByReceiverAndIsReadFalse(user).stream()
        .map(NotificationDto::fromEntity)
        .collect(Collectors.toList());
  }

  // 알림을 읽음 처리
  @Override
  public void markNotificationsAsRead(Long userId) {
    List<Notification> notifications = notificationRepository.findByReceiverAndIsReadFalse(
        userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_FOUND)));

    notifications.forEach(Notification::markAsRead);
    notificationRepository.saveAll(notifications);
  }
}
