package com.truckplatform.search.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchTruckRequest {
    
    @NotBlank(message = "Source city is required")
    private String sourceCity;
    
    @NotBlank(message = "Destination city is required")
    private String destinationCity;
    
    @Min(value = 1, message = "Weight must be greater than 0")
    private Long weight;
}
