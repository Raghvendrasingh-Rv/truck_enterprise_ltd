package com.truckplatform.pricing.service;

import com.truckplatform.pricing.dto.PricingRequest;
import com.truckplatform.pricing.dto.PricingResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PricingService {

    private static final BigDecimal RATE_PER_KM = new BigDecimal("0.50");
    private static final BigDecimal RATE_PER_KG = new BigDecimal("0.01");
    private static final BigDecimal MINIMUM_CHARGE = new BigDecimal("100");

    /**
     * Calculate pricing for a booking
     */
    public PricingResponse calculatePrice(PricingRequest request) {
        BigDecimal baseFare = MINIMUM_CHARGE;
        BigDecimal distanceFare = request.getDistanceKm().multiply(RATE_PER_KM);
        BigDecimal weightFare = new BigDecimal(request.getWeightKg()).multiply(RATE_PER_KG);

        BigDecimal totalFare = baseFare.add(distanceFare).add(weightFare);

        PricingResponse response = new PricingResponse();
        response.setBaseFare(baseFare);
        response.setDistanceFare(distanceFare);
        response.setWeightFare(weightFare);
        response.setTotalFare(totalFare);
        response.setBreakdown(String.format("Base: %s + Distance: %s + Weight: %s = Total: %s",
                baseFare, distanceFare, weightFare, totalFare));

        return response;
    }
}
