package com.truckplatform.notifications.controller;

import com.truckplatform.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Notification controller
 * Handles notification retrieval and management
 */
@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "*", maxAge = 3600)
public class NotificationController {

    /**
     * Placeholder endpoint for future implementation
     * This will be used to fetch notifications from the event bus
     */
    @GetMapping
    public ResponseEntity<ApiResponse<String>> getNotifications() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Notifications endpoint placeholder", "Notifications will be available after Kafka integration"));
    }
}
