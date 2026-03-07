package com.truckplatform.trucks.dto;

import com.truckplatform.trucks.entity.TruckType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTruckRequest {
    
    @NotBlank(message = "Truck number is required")
    private String truckNumber;
    
    @NotNull(message = "Truck type is required")
    private TruckType truckType;
    
    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be greater than 0")
    private Long capacityKg;
    
    @NotBlank(message = "Location city is required")
    private String locationCity;
}
