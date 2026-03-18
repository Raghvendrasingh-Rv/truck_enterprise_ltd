package com.truckplatform.bookings.dto;

import com.truckplatform.bookings.entity.BookingStatus;
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

    private Long truckId;

    private String source;

    private String destination;

    private Long weight;

    private BigDecimal price;

    private BookingStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
