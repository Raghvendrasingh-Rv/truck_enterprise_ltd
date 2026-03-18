package com.truckplatform.auth.service;

import com.truckplatform.auth.dto.LoginRequest;
import com.truckplatform.auth.dto.LoginResponse;
import com.truckplatform.auth.dto.RegisterRequest;
import com.truckplatform.auth.util.JwtTokenUtil;
import com.truckplatform.users.entity.User;
import com.truckplatform.users.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * Register a new user
     */
    @Transactional
    public User register(RegisterRequest registerRequest) {
        // Check if user already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        if (userRepository.existsByPhone(registerRequest.getPhone())) {
            throw new IllegalArgumentException("Phone number already registered");
        }

        // Create new user
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getPhone());
        user.setPasswordHash(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(registerRequest.getRole());

        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getEmail());
        
        return savedUser;
    }

    /**
     * Login user with email and password
     */
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            // Load user details
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            // Find user from database to get full details
            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Generate JWT token
            String role = user.getRole().name();
            String token = jwtTokenUtil.generateTokenWithClaims(userDetails, role);

            log.info("User login successful: {}", user.getEmail());

            // Create and return login response
            return new LoginResponse(
                    token,
                    user.getId(),
                    user.getEmail(),
                    user.getName(),
                    user.getRole(),
                    "Bearer"
            );
        } catch (Exception ex) {
            log.error("Authentication failed: {}", ex.getMessage());
            throw new RuntimeException("Invalid email or password");
        }
    }

    /**
     * Get user by email (for profile lookups)
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    /**
     * Change user password
     */
    @Transactional
    public void changePassword(String email, String oldPassword, String newPassword) {
        User user = getUserByEmail(email);

        // Verify old password
        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        // Update password
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        log.info("Password changed for user: {}", email);
    }
}
