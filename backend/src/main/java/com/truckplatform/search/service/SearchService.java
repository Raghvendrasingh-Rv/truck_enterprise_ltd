package com.truckplatform.search.service;

import com.truckplatform.search.dto.SearchTruckRequest;
import com.truckplatform.search.dto.SearchTruckResponse;
import com.truckplatform.trucks.entity.Truck;
import com.truckplatform.trucks.repository.TruckRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SearchService {

    @Autowired
    private TruckRepository truckRepository;

    /**
     * Search for available trucks based on criteria
     * - Searches in source city
     * - Filters by capacity >= weight
     * - Only returns AVAILABLE trucks
     * - Includes transporter information
     */
    @Transactional(readOnly = true)
    public List<SearchTruckResponse> searchAvailableTrucks(SearchTruckRequest request) {
        log.info("Searching trucks in {} with capacity >= {} kg", 
                request.getSourceCity(), request.getWeight());

        // Query optimized with fetch join to prevent N+1 problem
        List<Truck> availableTrucks = truckRepository.searchAvailableTrucks(
                request.getSourceCity(), 
                request.getWeight()
        );

        log.info("Found {} available trucks", availableTrucks.size());

        // Convert entities to response DTOs
        return availableTrucks.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convert Truck entity to SearchTruckResponse DTO
     */
    private SearchTruckResponse mapToResponse(Truck truck) {
        SearchTruckResponse response = new SearchTruckResponse();
        response.setTruckId(truck.getId());
        response.setTruckType(truck.getTruckType());
        response.setCapacity(truck.getCapacityKg());
        response.setLocationCity(truck.getLocationCity());
        
        // Get transporter details
        if (truck.getTransporter() != null) {
            response.setTransporterName(truck.getTransporter().getCompanyName());
            response.setTransporterRating(truck.getTransporter().getRating());
        }
        
        return response;
    }
}
