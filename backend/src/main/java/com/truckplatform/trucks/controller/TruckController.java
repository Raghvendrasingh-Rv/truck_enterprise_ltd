package com.truckplatform.trucks.controller;

import com.truckplatform.common.ApiResponse;
import com.truckplatform.trucks.dto.CreateTruckRequest;
import com.truckplatform.trucks.dto.TruckResponse;
import com.truckplatform.trucks.dto.UpdateTruckRequest;
import com.truckplatform.trucks.service.TruckService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trucks")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TruckController {
    
    private static final Logger log = LoggerFactory.getLogger(TruckController.class);

    @Autowired
    private TruckService truckService;

    /**
     * Add a new truck
     * POST /api/trucks
     */
    @PostMapping
    @PreAuthorize("hasAuthority('TRANSPORTER')")
    public ResponseEntity<ApiResponse<TruckResponse>> addTruck(
            @Valid @RequestBody CreateTruckRequest request,
            Authentication authentication) {
        try {
            String transporterEmail = authentication.getName();
            log.info("Adding truck: {} for transporter email: {}", request.getTruckNumber(), transporterEmail);
            
            TruckResponse truck = truckService.addTruck(transporterEmail, request);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Truck added successfully", truck));
        } catch (IllegalArgumentException ex) {
            log.warn("Validation error while adding truck: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, ex.getMessage(), null));
        } catch (Exception ex) {
            log.error("Error adding truck: ", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, ex.getMessage(), null));
        }
    }

    /**
     * Get all trucks for a transporter
     * GET /api/trucks/transporter/{transporterId}
     */
    @GetMapping("/transporter/{transporterId}")
    public ResponseEntity<ApiResponse<List<TruckResponse>>> getTrucks(@PathVariable Long transporterId) {
        try {
            log.info("Fetching trucks for transporter: {}", transporterId);
            
            List<TruckResponse> trucks = truckService.getTrucksForTransporter(transporterId);
            
            return ResponseEntity.ok()
                    .body(new ApiResponse<>(true, "Trucks retrieved successfully", trucks));
        } catch (Exception ex) {
            log.error("Error fetching trucks: ", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, ex.getMessage(), null));
        }
    }

    /**
     * Get a specific truck by ID
     * GET /api/trucks/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TruckResponse>> getTruckById(@PathVariable Long id) {
        try {
            log.info("Fetching truck: {}", id);
            
            TruckResponse truck = truckService.getTruckById(id);
            
            return ResponseEntity.ok()
                    .body(new ApiResponse<>(true, "Truck retrieved successfully", truck));
        } catch (RuntimeException ex) {
            log.warn("Truck not found or unauthorized: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, ex.getMessage(), null));
        } catch (Exception ex) {
            log.error("Error fetching truck: ", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, ex.getMessage(), null));
        }
    }

    /**
     * Update truck information
     * PUT /api/trucks/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('TRANSPORTER')")
    public ResponseEntity<ApiResponse<TruckResponse>> updateTruck(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTruckRequest request,
            Authentication authentication) {
        try {
            String transporterEmail = authentication.getName();
            log.info("Updating truck: {} for transporter email: {}", id, transporterEmail);
            
            TruckResponse truck = truckService.updateTruck(transporterEmail, id, request);
            
            return ResponseEntity.ok()
                    .body(new ApiResponse<>(true, "Truck updated successfully", truck));
        } catch (IllegalArgumentException ex) {
            log.warn("Validation error while updating truck: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, ex.getMessage(), null));
        } catch (RuntimeException ex) {
            log.warn("Truck not found or unauthorized: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, ex.getMessage(), null));
        } catch (Exception ex) {
            log.error("Error updating truck: ", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, ex.getMessage(), null));
        }
    }

    /**
     * Delete a truck
     * DELETE /api/trucks/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TRANSPORTER')")
    public ResponseEntity<ApiResponse<Void>> deleteTruck(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            String transporterEmail = authentication.getName();
            log.info("Deleting truck: {} for transporter email: {}", id, transporterEmail);
            
            truckService.deleteTruck(transporterEmail, id);
            
            return ResponseEntity.ok()
                    .body(new ApiResponse<>(true, "Truck deleted successfully", null));
        } catch (RuntimeException ex) {
            log.warn("Truck not found or unauthorized: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, ex.getMessage(), null));
        } catch (Exception ex) {
            log.error("Error deleting truck: ", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, ex.getMessage(), null));
        }
    }
}
