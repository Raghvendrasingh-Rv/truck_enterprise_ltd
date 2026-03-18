package com.truckplatform.tracking.controller;

import com.truckplatform.common.ApiResponse;
import com.truckplatform.tracking.dto.TrackingRequest;
import com.truckplatform.tracking.dto.TrackingResponse;
import com.truckplatform.tracking.service.TrackingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tracking")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TrackingController {

    @Autowired
    private TrackingService trackingService;

    /**
     * Create a new tracking event
     *
     * @param trackingRequest the tracking request
     * @return the created tracking response
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TrackingResponse>> createTracking(@Valid @RequestBody TrackingRequest trackingRequest) {
        try {
            TrackingResponse tracking = trackingService.createTracking(trackingRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Tracking event created successfully", tracking));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An error occurred while creating the tracking event", null));
        }
    }

    /**
     * Get tracking history for a booking
     *
     * @param bookingId the booking ID
     * @return list of tracking events
     */
    @GetMapping("/bookings/{bookingId}")
    public ResponseEntity<ApiResponse<List<TrackingResponse>>> getTrackingHistory(@PathVariable Long bookingId) {
        List<TrackingResponse> trackingHistory = trackingService.getTrackingHistory(bookingId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Tracking history retrieved successfully", trackingHistory));
    }
}
