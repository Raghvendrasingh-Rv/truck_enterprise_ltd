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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
     * @param bookingRequest the booking request containing truck ID, source, destination, and weight
     * @return the created booking response
     */
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(
            @Valid @RequestBody BookingRequest bookingRequest,
            Authentication authentication) {
        try {
            BookingResponse booking = bookingService.createBooking(authentication.getName(), bookingRequest);
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
    @PreAuthorize("hasRole('CUSTOMER') or hasAuthority('TRANSPORTER')")
    public ResponseEntity<ApiResponse<BookingResponse>> getBooking(
            @PathVariable Long bookingId,
            Authentication authentication) {
        try {
            BookingResponse booking = bookingService.getBooking(authentication, bookingId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Booking retrieved successfully", booking));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/customer/me")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getMyBookings(Authentication authentication) {
        List<BookingResponse> bookings = bookingService.getBookingsByCustomer(authentication.getName());
        return ResponseEntity.ok(new ApiResponse<>(true, "Bookings retrieved successfully", bookings));
    }

    @GetMapping("/transporter/me")
    @PreAuthorize("hasAuthority('TRANSPORTER')")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getTransporterBookings(Authentication authentication) {
        List<BookingResponse> bookings = bookingService.getBookingsByTransporter(authentication.getName());
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
    @PreAuthorize("hasAuthority('TRANSPORTER')")
    public ResponseEntity<ApiResponse<BookingResponse>> acceptBooking(
            @PathVariable Long bookingId,
            Authentication authentication) {
        try {
            BookingResponse booking = bookingService.acceptBooking(authentication.getName(), bookingId);
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
    @PreAuthorize("hasAuthority('TRANSPORTER')")
    public ResponseEntity<ApiResponse<BookingResponse>> startBooking(
            @PathVariable Long bookingId,
            Authentication authentication) {
        try {
            BookingResponse booking = bookingService.startBooking(authentication.getName(), bookingId);
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
    @PreAuthorize("hasAuthority('TRANSPORTER')")
    public ResponseEntity<ApiResponse<BookingResponse>> completeBooking(
            @PathVariable Long bookingId,
            Authentication authentication) {
        try {
            BookingResponse booking = bookingService.completeBooking(authentication.getName(), bookingId);
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
    @PreAuthorize("hasRole('CUSTOMER') or hasAuthority('TRANSPORTER')")
    public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(
            @PathVariable Long bookingId,
            Authentication authentication) {
        try {
            BookingResponse booking = bookingService.cancelBooking(authentication, bookingId);
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
