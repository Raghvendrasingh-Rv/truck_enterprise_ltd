package com.truckplatform.search.dto;

import com.truckplatform.trucks.entity.TruckType;
import com.truckplatform.trucks.entity.TruckStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchTruckResponse {
    
    private Long truckId;
    private String truckNumber;
    private TruckType truckType;
    private Long capacity;
    private String locationCity;
    private TruckStatus status;
    private Long transporterId;
    private String transporterName;
    private Double transporterRating;
    private Boolean transporterVerified;
    private String sourceCity;
    private String destinationCity;
    private BigDecimal estimatedDistanceKm;
    private BigDecimal estimatedPrice;
}
