package com.truckplatform.notifications.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {

    private Long id;

    private String recipientType;

    private Long recipientId;

    private Long bookingId;

    private String type;

    private String title;

    private String message;

    private Boolean read;

    private LocalDateTime createdAt;
}
