package com.truckplatform.trucks.dto;

import com.truckplatform.trucks.entity.TruckStatus;
import com.truckplatform.trucks.entity.TruckType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TruckResponse {
    
    private Long id;
    private String truckNumber;
    private TruckType truckType;
    private Long capacityKg;
    private String locationCity;
    private TruckStatus status;
    private Long transporterId;
}
