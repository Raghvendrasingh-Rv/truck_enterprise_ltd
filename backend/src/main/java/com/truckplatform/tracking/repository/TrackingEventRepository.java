package com.truckplatform.tracking.repository;

import com.truckplatform.tracking.entity.TrackingEvent;
import com.truckplatform.tracking.entity.TrackingEventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TrackingEventRepository extends JpaRepository<TrackingEvent, Long> {
    List<TrackingEvent> findByBookingId(Long bookingId);
    List<TrackingEvent> findByBookingIdOrderByTimestampDesc(Long bookingId);
    List<TrackingEvent> findByStatus(TrackingEventStatus status);
    List<TrackingEvent> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    List<TrackingEvent> findByBookingIdAndStatus(Long bookingId, TrackingEventStatus status);
}
