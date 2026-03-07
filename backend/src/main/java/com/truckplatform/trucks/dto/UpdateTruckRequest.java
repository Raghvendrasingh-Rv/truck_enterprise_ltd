package com.truckplatform.trucks.dto;

import com.truckplatform.trucks.entity.TruckStatus;
import com.truckplatform.trucks.entity.TruckType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTruckRequest {
    
    private String truckNumber;
    private TruckType truckType;
    
    @Min(value = 1, message = "Capacity must be greater than 0")
    private Long capacityKg;
    
    private String locationCity;
    private TruckStatus status;
}
