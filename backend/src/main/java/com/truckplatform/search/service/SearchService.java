package com.truckplatform.search.service;

import com.truckplatform.cities.entity.City;
import com.truckplatform.cities.service.CityService;
import com.truckplatform.pricing.dto.PricingRequest;
import com.truckplatform.pricing.dto.PricingResponse;
import com.truckplatform.pricing.service.PricingService;
import com.truckplatform.search.dto.SearchTruckRequest;
import com.truckplatform.search.dto.SearchTruckResponse;
import com.truckplatform.trucks.entity.Truck;
import com.truckplatform.trucks.repository.TruckRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SearchService {

    @Autowired
    private TruckRepository truckRepository;

    @Autowired
    private CityService cityService;

    @Autowired
    private PricingService pricingService;

    /**
     * Search for available trucks based on criteria
     * - Searches in source city
     * - Filters by capacity >= weight
     * - Only returns AVAILABLE trucks
     * - Includes transporter information
     */
    @Transactional(readOnly = true)
    public List<SearchTruckResponse> searchAvailableTrucks(SearchTruckRequest request) {
        City sourceCity = cityService.getByName(request.getSourceCity());
        City destinationCity = cityService.getByName(request.getDestinationCity());
        BigDecimal distanceKm = calculateDistanceKm(sourceCity, destinationCity);

        log.info("Searching trucks in {} with capacity >= {} kg for destination {}",
                sourceCity.getName(), request.getWeight(), destinationCity.getName());

        // Query optimized with fetch join to prevent N+1 problem
        List<Truck> availableTrucks = truckRepository.searchAvailableTrucks(
                sourceCity.getName(),
                request.getWeight()
        );

        log.info("Found {} available trucks", availableTrucks.size());

        // Convert entities to response DTOs
        return availableTrucks.stream()
                .map(truck -> mapToResponse(truck, sourceCity.getName(), destinationCity.getName(), request.getWeight(), distanceKm))
                .collect(Collectors.toList());
    }

    /**
     * Convert Truck entity to SearchTruckResponse DTO
     */
    private SearchTruckResponse mapToResponse(Truck truck, String sourceCity, String destinationCity, Long weight, BigDecimal distanceKm) {
        SearchTruckResponse response = new SearchTruckResponse();
        response.setTruckId(truck.getId());
        response.setTruckNumber(truck.getTruckNumber());
        response.setTruckType(truck.getTruckType());
        response.setCapacity(truck.getCapacityKg());
        response.setLocationCity(truck.getLocationCity());
        response.setStatus(truck.getStatus());
        response.setSourceCity(sourceCity);
        response.setDestinationCity(destinationCity);
        response.setEstimatedDistanceKm(distanceKm);

        PricingRequest pricingRequest = new PricingRequest();
        pricingRequest.setDistanceKm(distanceKm);
        pricingRequest.setWeightKg(weight);
        pricingRequest.setTruckType(truck.getTruckType().name());
        PricingResponse pricingResponse = pricingService.calculatePrice(pricingRequest);
        response.setEstimatedPrice(pricingResponse.getTotalFare());
        
        // Get transporter details
        if (truck.getTransporter() != null) {
            response.setTransporterId(truck.getTransporter().getId());
            response.setTransporterName(truck.getTransporter().getCompanyName());
            response.setTransporterRating(truck.getTransporter().getRating());
            response.setTransporterVerified(truck.getTransporter().getVerified());
        }
        
        return response;
    }

    private BigDecimal calculateDistanceKm(City sourceCity, City destinationCity) {
        double earthRadiusKm = 6371.0;
        double latDistance = Math.toRadians(destinationCity.getLatitude() - sourceCity.getLatitude());
        double lonDistance = Math.toRadians(destinationCity.getLongitude() - sourceCity.getLongitude());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(sourceCity.getLatitude()))
                * Math.cos(Math.toRadians(destinationCity.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadiusKm * c;
        return BigDecimal.valueOf(distance).setScale(2, RoundingMode.HALF_UP);
    }
}
