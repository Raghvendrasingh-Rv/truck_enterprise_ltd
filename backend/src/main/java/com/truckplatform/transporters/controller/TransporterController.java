package com.truckplatform.transporters.controller;

import com.truckplatform.common.ApiResponse;
import com.truckplatform.transporters.dto.TransporterResponse;
import com.truckplatform.transporters.dto.UpdateTransporterRequest;
import com.truckplatform.transporters.service.TransporterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("transporters")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TransporterController {

    @Autowired
    private TransporterService transporterService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TransporterResponse>>> getAllTransporters() {
        List<TransporterResponse> transporters = transporterService.getAllTransporters();
        return ResponseEntity.ok(new ApiResponse<>(true, "Transporters retrieved successfully", transporters));
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

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TransporterResponse>> updateTransporter(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTransporterRequest request) {
        try {
            TransporterResponse transporter = transporterService.updateTransporter(id, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Transporter updated successfully", transporter));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTransporter(@PathVariable Long id) {
        try {
            transporterService.deleteTransporter(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Transporter deleted successfully", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
