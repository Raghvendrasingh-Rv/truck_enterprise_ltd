package com.truckplatform.bookings.dto;

import com.truckplatform.bookings.entity.BookingStatus;
import com.truckplatform.trucks.entity.TruckStatus;
import com.truckplatform.trucks.entity.TruckType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {

    private Long id;

    private Long customerId;

    private String customerName;

    private String customerEmail;

    private String customerPhone;

    private Long truckId;

    private String truckNumber;

    private TruckType truckType;

    private Long truckCapacityKg;

    private String truckLocationCity;

    private TruckStatus truckStatus;

    private Long transporterId;

    private String transporterName;

    private String transporterCompanyName;

    private String transporterEmail;

    private String transporterMobileNumber;

    private Integer transporterYearsOfExperience;

    private String transporterAddress;

    private Double transporterRating;

    private Boolean transporterVerified;

    private String source;

    private String destination;

    private Long weight;

    private BigDecimal price;

    private BookingStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
