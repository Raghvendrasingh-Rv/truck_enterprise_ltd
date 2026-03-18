package com.truckplatform.pricing.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PricingRequest {

    @NotNull(message = "Distance in km is required")
    @Min(value = 1, message = "Distance must be positive")
    private BigDecimal distanceKm;

    @NotNull(message = "Weight in kg is required")
    @Min(value = 1, message = "Weight must be positive")
    private Long weightKg;

    @NotNull(message = "Truck type is required")
    private String truckType;

    public BigDecimal getDistanceKm() { return distanceKm; }
    public void setDistanceKm(BigDecimal distanceKm) { this.distanceKm = distanceKm; }
    public Long getWeightKg() { return weightKg; }
    public void setWeightKg(Long weightKg) { this.weightKg = weightKg; }
    public String getTruckType() { return truckType; }
    public void setTruckType(String truckType) { this.truckType = truckType; }
}
