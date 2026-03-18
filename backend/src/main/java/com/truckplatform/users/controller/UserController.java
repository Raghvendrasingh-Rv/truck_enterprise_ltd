package com.truckplatform.users.controller;

import com.truckplatform.common.ApiResponse;
import com.truckplatform.users.dto.UpdateProfileRequest;
import com.truckplatform.users.dto.UserProfileResponse;
import com.truckplatform.users.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Get user profile
     * GET /api/users/profile
     */
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getUserProfile(Authentication authentication) {
        try {
            String email = authentication.getName();
            UserProfileResponse profile = userService.getUserProfile(email);
            return ResponseEntity.ok(new ApiResponse<>(true, "Profile retrieved successfully", profile));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    /**
     * Update user profile
     * PUT /api/users/profile
     */
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateUserProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            Authentication authentication) {
        try {
            String email = authentication.getName();
            UserProfileResponse profile = userService.updateUserProfile(email, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Profile updated successfully", profile));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
