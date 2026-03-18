package com.truckplatform.bookings.controller;

import com.truckplatform.bookings.dto.BookingRequest;
import com.truckplatform.bookings.dto.BookingResponse;
import com.truckplatform.bookings.entity.BookingStatus;
import com.truckplatform.bookings.service.BookingService;
import com.truckplatform.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BookingController {

    @Autowired
    private BookingService bookingService;

    /**
     * Create a new booking
     *
     * @param bookingRequest the booking request containing customer ID, truck ID, source, destination, and weight
     * @return the created booking response
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(@Valid @RequestBody BookingRequest bookingRequest) {
        try {
            BookingResponse booking = bookingService.createBooking(bookingRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Booking created successfully", booking));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An error occurred while creating the booking", null));
        }
    }

    /**
     * Get booking by ID
     *
     * @param bookingId the booking ID
     * @return the booking response
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<ApiResponse<BookingResponse>> getBooking(@PathVariable Long bookingId) {
        try {
            BookingResponse booking = bookingService.getBooking(bookingId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Booking retrieved successfully", booking));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    /**
     * Get all bookings for a customer
     *
     * @param customerId the customer ID
     * @return list of booking responses
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getBookingsByCustomer(@PathVariable Long customerId) {
        List<BookingResponse> bookings = bookingService.getBookingsByCustomer(customerId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Bookings retrieved successfully", bookings));
    }

    /**
     * Get all bookings for a truck
     *
     * @param truckId the truck ID
     * @return list of booking responses
     */
    @GetMapping("/truck/{truckId}")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getBookingsByTruck(@PathVariable Long truckId) {
        List<BookingResponse> bookings = bookingService.getBookingsByTruck(truckId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Bookings retrieved successfully", bookings));
    }

    /**
     * Get all bookings by status
     *
     * @param status the booking status
     * @return list of booking responses
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getBookingsByStatus(@PathVariable String status) {
        try {
            BookingStatus bookingStatus = BookingStatus.valueOf(status.toUpperCase());
            List<BookingResponse> bookings = bookingService.getBookingsByStatus(bookingStatus);
            return ResponseEntity.ok(new ApiResponse<>(true, "Bookings retrieved successfully", bookings));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, "Invalid booking status", null));
        }
    }

    /**
     * Accept a booking (change status from CREATED to ACCEPTED)
     *
     * @param bookingId the booking ID
     * @return the updated booking response
     */
    @PutMapping("/{bookingId}/accept")
    public ResponseEntity<ApiResponse<BookingResponse>> acceptBooking(@PathVariable Long bookingId) {
        try {
            BookingResponse booking = bookingService.acceptBooking(bookingId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Booking accepted successfully", booking));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An error occurred while accepting the booking", null));
        }
    }

    /**
     * Start a booking (change status from ACCEPTED to IN_TRANSIT)
     *
     * @param bookingId the booking ID
     * @return the updated booking response
     */
    @PutMapping("/{bookingId}/start")
    public ResponseEntity<ApiResponse<BookingResponse>> startBooking(@PathVariable Long bookingId) {
        try {
            BookingResponse booking = bookingService.startBooking(bookingId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Booking started successfully", booking));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An error occurred while starting the booking", null));
        }
    }

    /**
     * Complete a booking (change status from IN_TRANSIT to DELIVERED)
     *
     * @param bookingId the booking ID
     * @return the updated booking response
     */
    @PutMapping("/{bookingId}/complete")
    public ResponseEntity<ApiResponse<BookingResponse>> completeBooking(@PathVariable Long bookingId) {
        try {
            BookingResponse booking = bookingService.completeBooking(bookingId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Booking completed successfully", booking));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An error occurred while completing the booking", null));
        }
    }

    /**
     * Cancel a booking (change status to CANCELLED)
     *
     * @param bookingId the booking ID
     * @return the updated booking response
     */
    @PutMapping("/{bookingId}/cancel")
    public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(@PathVariable Long bookingId) {
        try {
            BookingResponse booking = bookingService.cancelBooking(bookingId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Booking cancelled successfully", booking));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An error occurred while cancelling the booking", null));
        }
    }
}
