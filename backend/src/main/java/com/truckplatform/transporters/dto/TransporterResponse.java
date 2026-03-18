package com.truckplatform.transporters.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransporterResponse {

    private Long id;

    private Long userId;

    private String companyName;

    private Double rating;

    private Boolean verified;
}
