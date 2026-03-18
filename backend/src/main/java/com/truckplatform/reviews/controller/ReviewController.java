package com.truckplatform.reviews.controller;

import com.truckplatform.common.ApiResponse;
import com.truckplatform.reviews.dto.CreateReviewRequest;
import com.truckplatform.reviews.dto.ReviewResponse;
import com.truckplatform.reviews.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    /**
     * Create a review for a completed booking
     * POST /api/reviews
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @Valid @RequestBody CreateReviewRequest request,
            Authentication authentication) {
        try {
            String email = authentication.getName();
            ReviewResponse review = reviewService.createReview(email, request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Review created successfully", review));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    /**
     * Get reviews for a transporter
     * GET /api/reviews/transporter/{transporterId}
     */
    @GetMapping("/transporter/{transporterId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getTransporterReviews(
            @PathVariable Long transporterId) {
        List<ReviewResponse> reviews = reviewService.getTransporterReviews(transporterId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Reviews retrieved successfully", reviews));
    }
}
