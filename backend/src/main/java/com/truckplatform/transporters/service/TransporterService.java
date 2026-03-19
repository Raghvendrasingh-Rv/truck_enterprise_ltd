package com.truckplatform.transporters.service;

import com.truckplatform.transporters.dto.CreateTransporterRequest;
import com.truckplatform.transporters.dto.TransporterResponse;
import com.truckplatform.transporters.dto.UpdateTransporterRequest;
import com.truckplatform.transporters.entity.Transporter;
import com.truckplatform.transporters.repository.TransporterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransporterService {

    @Autowired
    private TransporterRepository transporterRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Create a transporter
     */
    public TransporterResponse createTransporter(CreateTransporterRequest request) {
        validateUniqueFields(request.getEmail(), request.getMobileNumber(), null);

        Transporter transporter = new Transporter();
        transporter.setCompanyName(request.getCompanyName().trim());
        transporter.setName(request.getName().trim());
        transporter.setEmail(request.getEmail().trim());
        transporter.setMobileNumber(request.getMobileNumber().trim());
        transporter.setYearsOfExperience(request.getYearsOfExperience());
        transporter.setAddress(request.getAddress().trim());
        transporter.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        transporter.setRating(0.0);
        transporter.setVerified(false);

        Transporter savedTransporter = transporterRepository.save(transporter);
        return mapToResponse(savedTransporter);
    }

    public List<TransporterResponse> getAllTransporters() {
        return transporterRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get transporter profile
     */
    public TransporterResponse getTransporterProfile(Long transporterId) {
        Transporter transporter = transporterRepository.findById(transporterId)
                .orElseThrow(() -> new IllegalArgumentException("Transporter not found"));
        return mapToResponse(transporter);
    }

    public TransporterResponse updateTransporter(Long transporterId, UpdateTransporterRequest request) {
        Transporter transporter = transporterRepository.findById(transporterId)
                .orElseThrow(() -> new IllegalArgumentException("Transporter not found"));

        validateUniqueFields(request.getEmail(), request.getMobileNumber(), transporterId);

        if (request.getCompanyName() != null && !request.getCompanyName().trim().isEmpty()) {
            transporter.setCompanyName(request.getCompanyName().trim());
        }
        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            transporter.setName(request.getName().trim());
        }
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            transporter.setEmail(request.getEmail().trim());
        }
        if (request.getMobileNumber() != null && !request.getMobileNumber().trim().isEmpty()) {
            transporter.setMobileNumber(request.getMobileNumber().trim());
        }
        if (request.getYearsOfExperience() != null) {
            transporter.setYearsOfExperience(request.getYearsOfExperience());
        }
        if (request.getAddress() != null && !request.getAddress().trim().isEmpty()) {
            transporter.setAddress(request.getAddress().trim());
        }
        if (request.getRating() != null) {
            transporter.setRating(request.getRating());
        }
        if (request.getVerified() != null) {
            transporter.setVerified(request.getVerified());
        }

        return mapToResponse(transporterRepository.save(transporter));
    }

    public void deleteTransporter(Long transporterId) {
        Transporter transporter = transporterRepository.findById(transporterId)
                .orElseThrow(() -> new IllegalArgumentException("Transporter not found"));
        transporterRepository.delete(transporter);
    }

    /**
     * Map Transporter entity to TransporterResponse DTO
     */
    private TransporterResponse mapToResponse(Transporter transporter) {
        TransporterResponse response = new TransporterResponse();
        response.setId(transporter.getId());
        response.setCompanyName(transporter.getCompanyName());
        response.setName(transporter.getName());
        response.setEmail(transporter.getEmail());
        response.setMobileNumber(transporter.getMobileNumber());
        response.setYearsOfExperience(transporter.getYearsOfExperience());
        response.setAddress(transporter.getAddress());
        response.setRating(transporter.getRating());
        response.setVerified(transporter.getVerified());
        response.setTruckIds(
                transporter.getTrucks().stream()
                        .map(truck -> truck.getId())
                        .collect(Collectors.toList())
        );
        return response;
    }

    private void validateUniqueFields(String email, String mobileNumber, Long currentTransporterId) {
        if (email != null && !email.trim().isEmpty()) {
            transporterRepository.findByEmailIgnoreCase(email.trim())
                    .filter(transporter -> !transporter.getId().equals(currentTransporterId))
                    .ifPresent(transporter -> {
                        throw new IllegalArgumentException("Transporter email already exists");
                    });
        }

        if (mobileNumber != null && !mobileNumber.trim().isEmpty()) {
            transporterRepository.findByMobileNumber(mobileNumber.trim())
                    .filter(transporter -> !transporter.getId().equals(currentTransporterId))
                    .ifPresent(transporter -> {
                        throw new IllegalArgumentException("Transporter mobile number already exists");
                    });
        }
    }
}
