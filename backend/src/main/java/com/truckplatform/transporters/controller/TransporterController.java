package com.truckplatform.transporters.controller;

import com.truckplatform.common.ApiResponse;
import com.truckplatform.transporters.dto.CreateTransporterRequest;
import com.truckplatform.transporters.dto.TransporterResponse;
import com.truckplatform.transporters.service.TransporterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("transporters")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TransporterController {

    @Autowired
    private TransporterService transporterService;

    /**
     * Create a transporter profile
     * POST /api/transporters
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TransporterResponse>> createTransporter(
            @Valid @RequestBody CreateTransporterRequest request,
            Authentication authentication) {
        try {
            String email = authentication.getName();
            TransporterResponse transporter = transporterService.createTransporter(email, request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Transporter profile created successfully", transporter));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    /**
     * Get transporter profile
     * GET /api/transporters/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TransporterResponse>> getTransporter(@PathVariable Long id) {
        try {
            TransporterResponse transporter = transporterService.getTransporterProfile(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Transporter retrieved successfully", transporter));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
