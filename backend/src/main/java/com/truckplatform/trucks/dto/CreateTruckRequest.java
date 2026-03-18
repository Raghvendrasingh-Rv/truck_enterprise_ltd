package com.truckplatform.trucks.dto;

import com.truckplatform.trucks.entity.TruckType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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

    // Explicit getters and setters to bypass Lombok processing
    public String getTruckNumber() {
        return truckNumber;
    }

    public void setTruckNumber(String truckNumber) {
        this.truckNumber = truckNumber;
    }

    public TruckType getTruckType() {
        return truckType;
    }

    public void setTruckType(TruckType truckType) {
        this.truckType = truckType;
    }

    public Long getCapacityKg() {
        return capacityKg;
    }

    public void setCapacityKg(Long capacityKg) {
        this.capacityKg = capacityKg;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }
}
