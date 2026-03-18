package com.truckplatform.trucks.dto;

import com.truckplatform.trucks.entity.TruckStatus;
import com.truckplatform.trucks.entity.TruckType;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTruckRequest {
    
    private String truckNumber;
    private TruckType truckType;
    
    @Min(value = 1, message = "Capacity must be greater than 0")
    private Long capacityKg;
    
    private String locationCity;
    private TruckStatus status;

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

    public TruckStatus getStatus() {
        return status;
    }

    public void setStatus(TruckStatus status) {
        this.status = status;
    }
}
