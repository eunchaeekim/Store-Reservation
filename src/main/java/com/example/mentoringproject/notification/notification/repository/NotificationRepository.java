package com.example.mentoringproject.notification.notification.repository;

import com.example.mentoringproject.notification.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

  Page<Notification> findAllByReceiverEmail(String email, Pageable pageable);
}
