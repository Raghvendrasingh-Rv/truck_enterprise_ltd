package com.truckplatform.pricing.service;

import com.truckplatform.pricing.dto.PricingRequest;
import com.truckplatform.pricing.dto.PricingResponse;
import com.truckplatform.trucks.entity.TruckType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PricingService {

    private static final BigDecimal RATE_PER_KG = new BigDecimal("0.01");
    private static final BigDecimal MINIMUM_CHARGE = new BigDecimal("100");

    /**
     * Calculate pricing for a booking
     */
    public PricingResponse calculatePrice(PricingRequest request) {
        BigDecimal ratePerKm = getRatePerKm(request.getTruckType());
        BigDecimal baseFare = MINIMUM_CHARGE;
        BigDecimal distanceFare = request.getDistanceKm().multiply(ratePerKm);
        BigDecimal weightFare = new BigDecimal(request.getWeightKg()).multiply(RATE_PER_KG);

        BigDecimal totalFare = baseFare.add(distanceFare).add(weightFare);

        PricingResponse response = new PricingResponse();
        response.setBaseFare(baseFare);
        response.setDistanceFare(distanceFare);
        response.setWeightFare(weightFare);
        response.setTotalFare(totalFare);
        response.setBreakdown(String.format(
                "Base: %s + Distance(%s @ %s/km): %s + Weight: %s = Total: %s",
                baseFare,
                request.getTruckType(),
                ratePerKm,
                distanceFare,
                weightFare,
                totalFare
        ));

        return response;
    }

    private BigDecimal getRatePerKm(String truckType) {
        TruckType resolvedType;
        try {
            resolvedType = TruckType.valueOf(truckType);
        } catch (IllegalArgumentException | NullPointerException ex) {
            return new BigDecimal("0.50");
        }

        return switch (resolvedType) {
            case PICKUP -> new BigDecimal("0.40");
            case BOX_TRUCK -> new BigDecimal("0.50");
            case FLATBED -> new BigDecimal("0.60");
            case DUMP_TRUCK -> new BigDecimal("0.65");
            case AUTO_CARRIER -> new BigDecimal("0.75");
            case TANKER -> new BigDecimal("0.80");
            case REFRIGERATED -> new BigDecimal("0.90");
        };
    }
}
