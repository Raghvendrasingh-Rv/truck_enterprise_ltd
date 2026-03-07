package com.truckplatform.bookings.repository;

import com.truckplatform.bookings.entity.Booking;
import com.truckplatform.bookings.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomerId(Long customerId);
    List<Booking> findByTruckId(Long truckId);
    List<Booking> findByStatus(BookingStatus status);
    List<Booking> findByCustomerIdAndStatus(Long customerId, BookingStatus status);
    List<Booking> findByTruckTransporterId(Long transporterId);
    Optional<Booking> findByIdAndCustomerId(Long bookingId, Long customerId);
}
