package com.foodri.foodreview.notification.service;

import com.foodri.foodreview.notification.dto.NotificationDto;
import com.foodri.foodreview.user.entity.User;
import java.util.List;

public interface NotificationService {

  void sendNotification(User receiver, User sender, String message);
  List<NotificationDto> getUnreadNotifications(Long userId);
  void markNotificationsAsRead(Long userId);

}
