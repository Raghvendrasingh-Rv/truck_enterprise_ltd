package com.truckplatform.auth.controller;

import com.truckplatform.auth.dto.LoginRequest;
import com.truckplatform.auth.dto.LoginResponse;
import com.truckplatform.auth.dto.RegisterRequest;
import com.truckplatform.auth.service.AuthService;
import com.truckplatform.common.ApiResponse;
import com.truckplatform.users.entity.User;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Register a new user
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            log.info("New registration request for email: {}", registerRequest.getEmail());
            User registeredUser = authService.register(registerRequest);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "User registered successfully", registeredUser));
        } catch (IllegalArgumentException ex) {
            log.warn("Registration failed: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, ex.getMessage(), null));
        } catch (Exception ex) {
            log.error("Registration error: ", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Registration failed: " + ex.getMessage(), null));
        }
    }

    /**
     * Login user
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            log.info("Login request for email: {}", loginRequest.getEmail());
            LoginResponse loginResponse = authService.login(loginRequest);
            
            return ResponseEntity.ok()
                    .body(new ApiResponse<>(true, "Login successful", loginResponse));
        } catch (RuntimeException ex) {
            log.warn("Login failed: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, ex.getMessage(), null));
        } catch (Exception ex) {
            log.error("Login error: ", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Login failed: " + ex.getMessage(), null));
        }
    }
}
