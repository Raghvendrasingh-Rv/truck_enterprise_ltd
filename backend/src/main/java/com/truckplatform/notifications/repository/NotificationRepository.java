package com.truckplatform.notifications.repository;

import com.truckplatform.notifications.entity.Notification;
import com.truckplatform.notifications.entity.NotificationRecipientType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientTypeAndRecipientIdOrderByCreatedAtDesc(NotificationRecipientType recipientType, Long recipientId);
    Page<Notification> findByRecipientTypeAndRecipientIdOrderByCreatedAtDesc(
            NotificationRecipientType recipientType,
            Long recipientId,
            Pageable pageable
    );
    long countByRecipientTypeAndRecipientIdAndReadFalse(NotificationRecipientType recipientType, Long recipientId);
}
