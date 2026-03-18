package com.truckplatform.bookings.service;

import com.truckplatform.bookings.dto.BookingRequest;
import com.truckplatform.bookings.dto.BookingResponse;
import com.truckplatform.bookings.entity.Booking;
import com.truckplatform.bookings.entity.BookingStatus;
import com.truckplatform.bookings.repository.BookingRepository;
import com.truckplatform.trucks.entity.Truck;
import com.truckplatform.trucks.repository.TruckRepository;
import com.truckplatform.users.entity.User;
import com.truckplatform.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TruckRepository truckRepository;

    /**
     * Create a new booking
     *
     * @param bookingRequest the booking request containing customer, truck, source, destination, and weight
     * @return the created booking response
     * @throws IllegalArgumentException if customer or truck is not found
     */
    public BookingResponse createBooking(BookingRequest bookingRequest) {
        // Validate customer exists
        User customer = userRepository.findById(bookingRequest.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        // Validate truck exists
        Truck truck = truckRepository.findById(bookingRequest.getTruckId())
                .orElseThrow(() -> new IllegalArgumentException("Truck not found"));

        // Create new booking
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setTruck(truck);
        booking.setSource(bookingRequest.getSource());
        booking.setDestination(bookingRequest.getDestination());
        booking.setWeight(bookingRequest.getWeight());
        booking.setStatus(BookingStatus.CREATED);
        
        // Set a default price (can be calculated based on distance, weight, etc.)
        booking.setPrice(BigDecimal.ZERO);

        // Save booking
        Booking savedBooking = bookingRepository.save(booking);

        return mapToResponse(savedBooking);
    }

    /**
     * Get booking by ID
     *
     * @param bookingId the booking ID
     * @return the booking response
     */
    public BookingResponse getBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        return mapToResponse(booking);
    }

    /**
     * Get all bookings for a customer
     *
     * @param customerId the customer ID
     * @return list of booking responses
     */
    public List<BookingResponse> getBookingsByCustomer(Long customerId) {
        List<Booking> bookings = bookingRepository.findByCustomerId(customerId);
        return bookings.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all bookings for a truck
     *
     * @param truckId the truck ID
     * @return list of booking responses
     */
    public List<BookingResponse> getBookingsByTruck(Long truckId) {
        List<Booking> bookings = bookingRepository.findByTruckId(truckId);
        return bookings.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all bookings with a specific status
     *
     * @param status the booking status
     * @return list of booking responses
     */
    public List<BookingResponse> getBookingsByStatus(BookingStatus status) {
        List<Booking> bookings = bookingRepository.findByStatus(status);
        return bookings.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Accept a booking (change status from CREATED to ACCEPTED)
     *
     * @param bookingId the booking ID
     * @return the updated booking response
     */
    public BookingResponse acceptBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (booking.getStatus() != BookingStatus.CREATED) {
            throw new IllegalArgumentException("Only bookings with CREATED status can be accepted");
        }

        booking.setStatus(BookingStatus.ACCEPTED);
        Booking updatedBooking = bookingRepository.save(booking);
        return mapToResponse(updatedBooking);
    }

    /**
     * Start a booking (change status from ACCEPTED to IN_TRANSIT)
     *
     * @param bookingId the booking ID
     * @return the updated booking response
     */
    public BookingResponse startBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (booking.getStatus() != BookingStatus.ACCEPTED) {
            throw new IllegalArgumentException("Only bookings with ACCEPTED status can be started");
        }

        booking.setStatus(BookingStatus.IN_TRANSIT);
        Booking updatedBooking = bookingRepository.save(booking);
        return mapToResponse(updatedBooking);
    }

    /**
     * Complete a booking (change status from IN_TRANSIT to DELIVERED)
     *
     * @param bookingId the booking ID
     * @return the updated booking response
     */
    public BookingResponse completeBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (booking.getStatus() != BookingStatus.IN_TRANSIT) {
            throw new IllegalArgumentException("Only bookings with IN_TRANSIT status can be completed");
        }

        booking.setStatus(BookingStatus.DELIVERED);
        Booking updatedBooking = bookingRepository.save(booking);
        return mapToResponse(updatedBooking);
    }

    /**
     * Cancel a booking (change status to CANCELLED)
     *
     * @param bookingId the booking ID
     * @return the updated booking response
     */
    public BookingResponse cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (booking.getStatus() == BookingStatus.CANCELLED || booking.getStatus() == BookingStatus.DELIVERED) {
            throw new IllegalArgumentException("Booking cannot be cancelled in current status");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        Booking updatedBooking = bookingRepository.save(booking);
        return mapToResponse(updatedBooking);
    }

    /**
     * Map Booking entity to BookingResponse DTO
     *
     * @param booking the booking entity
     * @return the booking response
     */
    private BookingResponse mapToResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setCustomerId(booking.getCustomer().getId());
        response.setTruckId(booking.getTruck().getId());
        response.setSource(booking.getSource());
        response.setDestination(booking.getDestination());
        response.setWeight(booking.getWeight());
        response.setPrice(booking.getPrice());
        response.setStatus(booking.getStatus());
        response.setCreatedAt(booking.getCreatedAt());
        response.setUpdatedAt(booking.getUpdatedAt());
        return response;
    }
}
