package com.truckplatform.tracking.service;

import com.truckplatform.bookings.entity.Booking;
import com.truckplatform.bookings.repository.BookingRepository;
import com.truckplatform.tracking.dto.TrackingRequest;
import com.truckplatform.tracking.dto.TrackingResponse;
import com.truckplatform.tracking.entity.TrackingEvent;
import com.truckplatform.tracking.entity.TrackingEventStatus;
import com.truckplatform.tracking.repository.TrackingEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrackingService {

    @Autowired
    private TrackingEventRepository trackingEventRepository;

    @Autowired
    private BookingRepository bookingRepository;

    /**
     * Create a new tracking event
     *
     * @param trackingRequest the tracking request
     * @return the tracking response
     */
    public TrackingResponse createTracking(TrackingRequest trackingRequest) {
        Booking booking = bookingRepository.findById(trackingRequest.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        try {
            TrackingEventStatus status = TrackingEventStatus.valueOf(trackingRequest.getStatus().toUpperCase());

            TrackingEvent trackingEvent = new TrackingEvent();
            trackingEvent.setBooking(booking);
            trackingEvent.setLocation(trackingRequest.getLocation());
            trackingEvent.setStatus(status);

            TrackingEvent savedEvent = trackingEventRepository.save(trackingEvent);
            return mapToResponse(savedEvent);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid tracking status");
        }
    }

    /**
     * Get tracking history for a booking
     *
     * @param bookingId the booking ID
     * @return list of tracking events in descending order by timestamp
     */
    public List<TrackingResponse> getTrackingHistory(Long bookingId) {
        List<TrackingEvent> events = trackingEventRepository.findByBookingIdOrderByTimestampDesc(bookingId);
        return events.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Map TrackingEvent entity to TrackingResponse DTO
     *
     * @param trackingEvent the tracking event
     * @return the tracking response
     */
    private TrackingResponse mapToResponse(TrackingEvent trackingEvent) {
        TrackingResponse response = new TrackingResponse();
        response.setId(trackingEvent.getId());
        response.setBookingId(trackingEvent.getBooking().getId());
        response.setLocation(trackingEvent.getLocation());
        response.setStatus(trackingEvent.getStatus());
        response.setTimestamp(trackingEvent.getTimestamp());
        return response;
    }
}
