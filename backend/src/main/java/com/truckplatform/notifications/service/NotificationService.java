package com.truckplatform.notifications.service;

import com.truckplatform.notifications.NotificationConstants;
import com.truckplatform.notifications.dto.NotificationResponse;
import com.truckplatform.notifications.entity.Notification;
import com.truckplatform.notifications.entity.NotificationRecipientType;
import com.truckplatform.notifications.repository.NotificationRepository;
import com.truckplatform.transporters.entity.Transporter;
import com.truckplatform.transporters.repository.TransporterRepository;
import com.truckplatform.users.entity.User;
import com.truckplatform.users.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Notification service for sending notifications and updates
 */
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final TransporterRepository transporterRepository;

    public NotificationService(
            NotificationRepository notificationRepository,
            UserRepository userRepository,
            TransporterRepository transporterRepository
    ) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.transporterRepository = transporterRepository;
    }

    /**
     * Notify transporter about a new booking request
     */
    public void notifyBookingCreated(Long bookingId, Long transporterId) {
        saveNotification(
                NotificationRecipientType.TRANSPORTER,
                transporterId,
                bookingId,
                NotificationConstants.BOOKING_CREATED,
                "New booking request",
                "A customer has created a new booking request for one of your trucks."
        );
    }

    /**
     * Notify customer when booking is accepted
     */
    public void notifyBookingAccepted(Long bookingId, Long customerId) {
        saveNotification(
                NotificationRecipientType.CUSTOMER,
                customerId,
                bookingId,
                NotificationConstants.BOOKING_ACCEPTED,
                "Booking accepted",
                "Your booking has been accepted by the transporter."
        );
    }

    /**
     * Notify customer when booking moves to in-transit
     */
    public void notifyBookingInTransit(Long bookingId, Long customerId) {
        saveNotification(
                NotificationRecipientType.CUSTOMER,
                customerId,
                bookingId,
                NotificationConstants.BOOKING_STARTED,
                "Shipment in transit",
                "Your shipment is now in transit."
        );
    }

    /**
     * Notify customer when booking is delivered
     */
    public void notifyBookingDelivered(Long bookingId, Long customerId) {
        saveNotification(
                NotificationRecipientType.CUSTOMER,
                customerId,
                bookingId,
                NotificationConstants.BOOKING_DELIVERED,
                "Shipment delivered",
                "Your shipment has been delivered successfully."
        );
    }

    /**
     * Notify customer or transporter when booking is cancelled
     */
    public void notifyBookingCancelledToCustomer(Long bookingId, Long customerId) {
        saveNotification(
                NotificationRecipientType.CUSTOMER,
                customerId,
                bookingId,
                NotificationConstants.BOOKING_CANCELLED,
                "Booking cancelled",
                "Your booking has been cancelled."
        );
    }

    public void notifyBookingCancelledToTransporter(Long bookingId, Long transporterId) {
        saveNotification(
                NotificationRecipientType.TRANSPORTER,
                transporterId,
                bookingId,
                NotificationConstants.BOOKING_CANCELLED,
                "Booking cancelled",
                "A booking assigned to your truck has been cancelled."
        );
    }

    public List<NotificationResponse> getNotificationsForCurrentActor(Authentication authentication) {
        return getNotificationsForCurrentActor(authentication, 0, 20);
    }

    public List<NotificationResponse> getNotificationsForCurrentActor(Authentication authentication, int page, int size) {
        NotificationRecipientType recipientType = authentication.getAuthorities().stream()
                .anyMatch(authority -> "TRANSPORTER".equals(authority.getAuthority()))
                ? NotificationRecipientType.TRANSPORTER
                : NotificationRecipientType.CUSTOMER;

        Long recipientId = resolveRecipientId(authentication.getName(), recipientType);

        return notificationRepository.findByRecipientTypeAndRecipientIdOrderByCreatedAtDesc(
                        recipientType,
                        recipientId,
                        PageRequest.of(page, size)
                )
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public long getUnreadCount(Authentication authentication) {
        NotificationRecipientType recipientType = authentication.getAuthorities().stream()
                .anyMatch(authority -> "TRANSPORTER".equals(authority.getAuthority()))
                ? NotificationRecipientType.TRANSPORTER
                : NotificationRecipientType.CUSTOMER;
        Long recipientId = resolveRecipientId(authentication.getName(), recipientType);
        return notificationRepository.countByRecipientTypeAndRecipientIdAndReadFalse(recipientType, recipientId);
    }

    public NotificationResponse markAsRead(Authentication authentication, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

        NotificationRecipientType recipientType = authentication.getAuthorities().stream()
                .anyMatch(authority -> "TRANSPORTER".equals(authority.getAuthority()))
                ? NotificationRecipientType.TRANSPORTER
                : NotificationRecipientType.CUSTOMER;
        Long recipientId = resolveRecipientId(authentication.getName(), recipientType);

        if (!notification.getRecipientType().equals(recipientType) || !notification.getRecipientId().equals(recipientId)) {
            throw new IllegalArgumentException("Notification does not belong to the current actor");
        }

        notification.setRead(true);
        return mapToResponse(notificationRepository.save(notification));
    }

    public void markAllAsRead(Authentication authentication) {
        NotificationRecipientType recipientType = authentication.getAuthorities().stream()
                .anyMatch(authority -> "TRANSPORTER".equals(authority.getAuthority()))
                ? NotificationRecipientType.TRANSPORTER
                : NotificationRecipientType.CUSTOMER;
        Long recipientId = resolveRecipientId(authentication.getName(), recipientType);

        List<Notification> notifications = notificationRepository
                .findByRecipientTypeAndRecipientIdOrderByCreatedAtDesc(recipientType, recipientId);

        notifications.stream()
                .filter(notification -> !notification.getRead())
                .forEach(notification -> notification.setRead(true));

        notificationRepository.saveAll(notifications);
    }

    private Long resolveRecipientId(String principal, NotificationRecipientType recipientType) {
        if (recipientType == NotificationRecipientType.TRANSPORTER) {
            Transporter transporter = transporterRepository.findByEmailIgnoreCase(principal)
                    .orElseThrow(() -> new IllegalArgumentException("Transporter not found"));
            return transporter.getId();
        }

        User user = userRepository.findByEmail(principal)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        return user.getId();
    }

    private void saveNotification(
            NotificationRecipientType recipientType,
            Long recipientId,
            Long bookingId,
            String type,
            String title,
            String message
    ) {
        Notification notification = new Notification();
        notification.setRecipientType(recipientType);
        notification.setRecipientId(recipientId);
        notification.setBookingId(bookingId);
        notification.setType(type);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setRead(false);
        notificationRepository.save(notification);
    }

    private NotificationResponse mapToResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getRecipientType().name(),
                notification.getRecipientId(),
                notification.getBookingId(),
                notification.getType(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getRead(),
                notification.getCreatedAt()
        );
    }
}
