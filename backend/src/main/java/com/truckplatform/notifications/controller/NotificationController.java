package com.truckplatform.notifications.controller;

import com.truckplatform.common.ApiResponse;
import com.truckplatform.notifications.dto.NotificationResponse;
import com.truckplatform.notifications.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Notification controller
 * Handles notification retrieval and management
 */
@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "*", maxAge = 3600)
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER') or hasAuthority('TRANSPORTER')")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getNotifications(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        List<NotificationResponse> notifications = notificationService.getNotificationsForCurrentActor(authentication, page, size);
        return ResponseEntity.ok(new ApiResponse<>(true, "Notifications retrieved successfully", notifications));
    }

    @GetMapping("/unread-count")
    @PreAuthorize("hasRole('CUSTOMER') or hasAuthority('TRANSPORTER')")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(Authentication authentication) {
        long unreadCount = notificationService.getUnreadCount(authentication);
        return ResponseEntity.ok(new ApiResponse<>(true, "Unread notification count retrieved successfully", unreadCount));
    }

    @PutMapping("/{notificationId}/read")
    @PreAuthorize("hasRole('CUSTOMER') or hasAuthority('TRANSPORTER')")
    public ResponseEntity<ApiResponse<NotificationResponse>> markAsRead(
            Authentication authentication,
            @PathVariable Long notificationId
    ) {
        NotificationResponse response = notificationService.markAsRead(authentication, notificationId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Notification marked as read", response));
    }

    @PutMapping("/read-all")
    @PreAuthorize("hasRole('CUSTOMER') or hasAuthority('TRANSPORTER')")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(Authentication authentication) {
        notificationService.markAllAsRead(authentication);
        return ResponseEntity.ok(new ApiResponse<>(true, "All notifications marked as read", null));
    }
}
