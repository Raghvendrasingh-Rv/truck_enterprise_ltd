package com.truckplatform.users.service;

import com.truckplatform.users.dto.UpdateProfileRequest;
import com.truckplatform.users.dto.UserProfileResponse;
import com.truckplatform.users.entity.User;
import com.truckplatform.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Get user profile
     */
    public UserProfileResponse getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return mapToResponse(user);
    }

    /**
     * Update user profile
     */
    public UserProfileResponse updateUserProfile(String email, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }

        User updatedUser = userRepository.save(user);
        return mapToResponse(updatedUser);
    }

    /**
     * Map User entity to UserProfileResponse DTO
     */
    private UserProfileResponse mapToResponse(User user) {
        UserProfileResponse response = new UserProfileResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole().toString());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}
