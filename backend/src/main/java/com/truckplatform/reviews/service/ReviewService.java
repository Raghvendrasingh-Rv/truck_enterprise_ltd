package com.truckplatform.reviews.service;

import com.truckplatform.bookings.entity.Booking;
import com.truckplatform.bookings.repository.BookingRepository;
import com.truckplatform.reviews.dto.CreateReviewRequest;
import com.truckplatform.reviews.dto.ReviewResponse;
import com.truckplatform.reviews.entity.Review;
import com.truckplatform.reviews.repository.ReviewRepository;
import com.truckplatform.transporters.entity.Transporter;
import com.truckplatform.transporters.repository.TransporterRepository;
import com.truckplatform.users.entity.User;
import com.truckplatform.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TransporterRepository transporterRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Create a review for a completed booking
     */
    public ReviewResponse createReview(String email, CreateReviewRequest request) {
        User reviewer = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        Transporter transporter = transporterRepository.findById(request.getTransporterId())
                .orElseThrow(() -> new IllegalArgumentException("Transporter not found"));

        Review review = new Review();
        review.setBooking(booking);
        review.setTransporter(transporter);
        review.setReviewer(reviewer);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review savedReview = reviewRepository.save(review);

        // Update transporter rating
        updateTransporterRating(transporter.getId());

        return mapToResponse(savedReview);
    }

    /**
     * Get reviews for a transporter
     */
    public List<ReviewResponse> getTransporterReviews(Long transporterId) {
        List<Review> reviews = reviewRepository.findByTransporterId(transporterId);
        return reviews.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Update transporter average rating
     */
    private void updateTransporterRating(Long transporterId) {
        Transporter transporter = transporterRepository.findById(transporterId)
                .orElseThrow(() -> new IllegalArgumentException("Transporter not found"));

        List<Review> reviews = reviewRepository.findByTransporterId(transporterId);
        if (!reviews.isEmpty()) {
            double averageRating = reviews.stream()
                    .mapToDouble(Review::getRating)
                    .average()
                    .orElse(0.0);
            transporter.setRating(averageRating);
            transporterRepository.save(transporter);
        }
    }

    /**
     * Map Review entity to ReviewResponse DTO
     */
    private ReviewResponse mapToResponse(Review review) {
        ReviewResponse response = new ReviewResponse();
        response.setId(review.getId());
        response.setBookingId(review.getBooking().getId());
        response.setTransporterId(review.getTransporter().getId());
        response.setReviewerId(review.getReviewer().getId());
        response.setRating(review.getRating());
        response.setComment(review.getComment());
        response.setCreatedAt(review.getCreatedAt());
        return response;
    }
}
