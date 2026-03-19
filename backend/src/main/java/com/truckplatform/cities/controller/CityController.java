package com.truckplatform.cities.controller;

import com.truckplatform.cities.dto.CreateCityRequest;
import com.truckplatform.cities.dto.CityResponse;
import com.truckplatform.cities.service.CityService;
import com.truckplatform.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cities")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CityResponse>>> getSupportedCities() {
        List<CityResponse> cities = cityService.getSupportedCities();
        log.info("Returning {} supported cities", cities.size());
        return ResponseEntity.ok(new ApiResponse<>(true, "Supported cities retrieved successfully", cities));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CityResponse>> createCity(@Valid @RequestBody CreateCityRequest request) {
        try {
            CityResponse city = cityService.createCity(request);
            log.info("Created supported city: {}", city.getName());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "City created successfully", city));
        } catch (IllegalArgumentException ex) {
            log.warn("Unable to create city: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, ex.getMessage(), null));
        }
    }
}
