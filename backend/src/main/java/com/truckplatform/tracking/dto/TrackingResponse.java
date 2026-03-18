package com.truckplatform.tracking.dto;

import com.truckplatform.tracking.entity.TrackingEventStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackingResponse {

    private Long id;

    private Long bookingId;

    private String location;

    private TrackingEventStatus status;

    private LocalDateTime timestamp;
}
