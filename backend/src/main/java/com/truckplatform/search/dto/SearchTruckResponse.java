package com.truckplatform.search.dto;

import com.truckplatform.trucks.entity.TruckType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchTruckResponse {
    
    private Long truckId;
    private TruckType truckType;
    private Long capacity;
    private String transporterName;
    private Double transporterRating;
    private String locationCity;
}
