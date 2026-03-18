package com.truckplatform.transporters.service;

import com.truckplatform.transporters.dto.CreateTransporterRequest;
import com.truckplatform.transporters.dto.TransporterResponse;
import com.truckplatform.transporters.entity.Transporter;
import com.truckplatform.transporters.repository.TransporterRepository;
import com.truckplatform.users.entity.User;
import com.truckplatform.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransporterService {

    @Autowired
    private TransporterRepository transporterRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Create a transporter profile
     */
    public TransporterResponse createTransporter(String email, CreateTransporterRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if transporter already exists
        if (transporterRepository.findByUserId(user.getId()).isPresent()) {
            throw new IllegalArgumentException("Transporter profile already exists for this user");
        }

        Transporter transporter = new Transporter();
        transporter.setUser(user);
        transporter.setCompanyName(request.getCompanyName());
        transporter.setRating(0.0);
        transporter.setVerified(false);

        Transporter savedTransporter = transporterRepository.save(transporter);
        return mapToResponse(savedTransporter);
    }

    /**
     * Get transporter profile
     */
    public TransporterResponse getTransporterProfile(Long transporterId) {
        Transporter transporter = transporterRepository.findById(transporterId)
                .orElseThrow(() -> new IllegalArgumentException("Transporter not found"));
        return mapToResponse(transporter);
    }

    /**
     * Map Transporter entity to TransporterResponse DTO
     */
    private TransporterResponse mapToResponse(Transporter transporter) {
        TransporterResponse response = new TransporterResponse();
        response.setId(transporter.getId());
        response.setUserId(transporter.getUser().getId());
        response.setCompanyName(transporter.getCompanyName());
        response.setRating(transporter.getRating());
        response.setVerified(transporter.getVerified());
        return response;
    }
}
