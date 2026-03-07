package com.truckplatform.trucks.service;

import com.truckplatform.trucks.dto.CreateTruckRequest;
import com.truckplatform.trucks.dto.TruckResponse;
import com.truckplatform.trucks.dto.UpdateTruckRequest;
import com.truckplatform.trucks.entity.Truck;
import com.truckplatform.trucks.entity.TruckStatus;
import com.truckplatform.trucks.repository.TruckRepository;
import com.truckplatform.transporters.entity.Transporter;
import com.truckplatform.transporters.repository.TransporterRepository;
import com.truckplatform.users.entity.User;
import com.truckplatform.users.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TruckService {

    @Autowired
    private TruckRepository truckRepository;

    @Autowired
    private TransporterRepository transporterRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Add a new truck for a transporter
     */
    @Transactional
    public TruckResponse addTruck(String email, CreateTruckRequest request) {
        // Get user and transporter
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Transporter transporter = transporterRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Transporter profile not found. Please create a transporter profile first."));

        // Check if truck number already exists
        if (truckRepository.findByTruckNumber(request.getTruckNumber()).isPresent()) {
            throw new IllegalArgumentException("Truck number already exists");
        }

        // Create new truck
        Truck truck = new Truck();
        truck.setTransporter(transporter);
        truck.setTruckNumber(request.getTruckNumber());
        truck.setTruckType(request.getTruckType());
        truck.setCapacityKg(request.getCapacityKg());
        truck.setLocationCity(request.getLocationCity());
        truck.setStatus(TruckStatus.AVAILABLE);

        Truck savedTruck = truckRepository.save(truck);
        log.info("Truck added: {} for transporter: {}", savedTruck.getTruckNumber(), transporter.getCompanyName());

        return mapToResponse(savedTruck);
    }

    /**
     * Get all trucks for a transporter
     */
    public List<TruckResponse> getTrucksForTransporter(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Transporter transporter = transporterRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Transporter profile not found"));

        List<Truck> trucks = truckRepository.findByTransporterId(transporter.getId());
        log.info("Retrieved {} trucks for transporter: {}", trucks.size(), transporter.getCompanyName());

        return trucks.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get a specific truck by ID (verify ownership)
     */
    public TruckResponse getTruckById(String email, Long truckId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Transporter transporter = transporterRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Transporter profile not found"));

        Truck truck = truckRepository.findById(truckId)
                .orElseThrow(() -> new RuntimeException("Truck not found"));

        // Verify truck belongs to transporter
        if (!truck.getTransporter().getId().equals(transporter.getId())) {
            throw new RuntimeException("Unauthorized: Truck does not belong to your transporter account");
        }

        return mapToResponse(truck);
    }

    /**
     * Update truck information
     */
    @Transactional
    public TruckResponse updateTruck(String email, Long truckId, UpdateTruckRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Transporter transporter = transporterRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Transporter profile not found"));

        Truck truck = truckRepository.findById(truckId)
                .orElseThrow(() -> new RuntimeException("Truck not found"));

        // Verify truck belongs to transporter
        if (!truck.getTransporter().getId().equals(transporter.getId())) {
            throw new RuntimeException("Unauthorized: Truck does not belong to your transporter account");
        }

        // Update fields if provided
        if (request.getTruckNumber() != null) {
            // Check if new truck number is unique (excluding current truck)
            if (!request.getTruckNumber().equals(truck.getTruckNumber()) &&
                truckRepository.findByTruckNumber(request.getTruckNumber()).isPresent()) {
                throw new IllegalArgumentException("Truck number already exists");
            }
            truck.setTruckNumber(request.getTruckNumber());
        }

        if (request.getTruckType() != null) {
            truck.setTruckType(request.getTruckType());
        }

        if (request.getCapacityKg() != null) {
            truck.setCapacityKg(request.getCapacityKg());
        }

        if (request.getLocationCity() != null) {
            truck.setLocationCity(request.getLocationCity());
        }

        if (request.getStatus() != null) {
            truck.setStatus(request.getStatus());
        }

        Truck updatedTruck = truckRepository.save(truck);
        log.info("Truck updated: {}", updatedTruck.getTruckNumber());

        return mapToResponse(updatedTruck);
    }

    /**
     * Delete a truck
     */
    @Transactional
    public void deleteTruck(String email, Long truckId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Transporter transporter = transporterRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Transporter profile not found"));

        Truck truck = truckRepository.findById(truckId)
                .orElseThrow(() -> new RuntimeException("Truck not found"));

        // Verify truck belongs to transporter
        if (!truck.getTransporter().getId().equals(transporter.getId())) {
            throw new RuntimeException("Unauthorized: Truck does not belong to your transporter account");
        }

        truckRepository.delete(truck);
        log.info("Truck deleted: {}", truck.getTruckNumber());
    }

    /**
     * Convert Truck entity to TruckResponse DTO
     */
    private TruckResponse mapToResponse(Truck truck) {
        TruckResponse response = new TruckResponse();
        response.setId(truck.getId());
        response.setTruckNumber(truck.getTruckNumber());
        response.setTruckType(truck.getTruckType());
        response.setCapacityKg(truck.getCapacityKg());
        response.setLocationCity(truck.getLocationCity());
        response.setStatus(truck.getStatus());
        response.setTransporterId(truck.getTransporter().getId());
        return response;
    }
}
