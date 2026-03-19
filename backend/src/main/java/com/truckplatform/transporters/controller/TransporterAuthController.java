package com.truckplatform.transporters.controller;

import com.truckplatform.common.ApiResponse;
import com.truckplatform.transporters.dto.CreateTransporterRequest;
import com.truckplatform.transporters.dto.TransporterLoginRequest;
import com.truckplatform.transporters.dto.TransporterLoginResponse;
import com.truckplatform.transporters.dto.TransporterResponse;
import com.truckplatform.transporters.service.TransporterAuthService;
import com.truckplatform.transporters.service.TransporterService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transporter-auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class TransporterAuthController {

    @Autowired
    private TransporterAuthService transporterAuthService;

    @Autowired
    private TransporterService transporterService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<TransporterResponse>> register(@Valid @RequestBody CreateTransporterRequest request) {
        try {
            log.info("Transporter registration request for email: {}", request.getEmail());
            TransporterResponse response = transporterService.createTransporter(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Transporter registered successfully", response));
        } catch (IllegalArgumentException ex) {
            log.warn("Transporter registration failed: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, ex.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TransporterLoginResponse>> login(@Valid @RequestBody TransporterLoginRequest request) {
        try {
            log.info("Transporter login request for email: {}", request.getEmail());
            TransporterLoginResponse response = transporterAuthService.login(request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Transporter login successful", response));
        } catch (RuntimeException ex) {
            log.warn("Transporter login failed: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, ex.getMessage(), null));
        }
    }
}
