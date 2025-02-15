package com.foodri.foodreview.notification.repository;

import com.foodri.foodreview.notification.entity.Notification;
import com.foodri.foodreview.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
  List<Notification> findByReceiverAndIsReadFalse(User receiver);
}