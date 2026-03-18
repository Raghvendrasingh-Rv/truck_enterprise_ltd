package com.truckplatform.search.controller;

import com.truckplatform.common.ApiResponse;
import com.truckplatform.search.dto.SearchTruckRequest;
import com.truckplatform.search.dto.SearchTruckResponse;
import com.truckplatform.search.service.SearchService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class SearchController {

    @Autowired
    private SearchService searchService;

    /**
     * Search for available trucks
     * POST /api/search
     * 
     * Request body:
     * {
     *   "sourceCity": "Mumbai",
     *   "destinationCity": "Bangalore",
     *   "weight": 5000
     * }
     */
    @PostMapping
    public ResponseEntity<ApiResponse<List<SearchTruckResponse>>> searchTrucks(
            @Valid @RequestBody SearchTruckRequest request) {
        try {
            log.info("Search request: source={}, destination={}, weight={}", 
                    request.getSourceCity(), request.getDestinationCity(), request.getWeight());

            List<SearchTruckResponse> results = searchService.searchAvailableTrucks(request);

            String message = String.format("Found %d available trucks", results.size());
            
            return ResponseEntity.ok()
                    .body(new ApiResponse<>(true, message, results));
        } catch (Exception ex) {
            log.error("Error searching trucks: ", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, ex.getMessage(), null));
        }
    }
}
