package com.truckplatform.pricing.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PricingResponse {

    private BigDecimal baseFare;

    private BigDecimal distanceFare;

    private BigDecimal weightFare;

    private BigDecimal totalFare;

    private String breakdown;
}
