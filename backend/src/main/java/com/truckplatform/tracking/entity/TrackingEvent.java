package com.truckplatform.tracking.entity;

import com.truckplatform.bookings.entity.Booking;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "tracking_events")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackingEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @NotBlank(message = "Location is required")
    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrackingEventStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public TrackingEventStatus getStatus() { return status; }
    public void setStatus(TrackingEventStatus status) { this.status = status; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
