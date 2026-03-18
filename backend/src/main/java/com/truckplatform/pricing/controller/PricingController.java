package com.truckplatform.pricing.controller;

import com.truckplatform.common.ApiResponse;
import com.truckplatform.pricing.dto.PricingRequest;
import com.truckplatform.pricing.dto.PricingResponse;
import com.truckplatform.pricing.service.PricingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pricing")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PricingController {

    @Autowired
    private PricingService pricingService;

    /**
     * Calculate pricing for a booking
     * POST /api/pricing
     */
    @PostMapping
    public ResponseEntity<ApiResponse<PricingResponse>> calculatePrice(@Valid @RequestBody PricingRequest request) {
        try {
            PricingResponse pricing = pricingService.calculatePrice(request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Price calculated successfully", pricing));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error calculating price", null));
        }
    }
}
