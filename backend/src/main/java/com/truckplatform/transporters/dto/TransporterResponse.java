package com.truckplatform.transporters.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransporterResponse {

    private Long id;

    private String companyName;
    private String name;
    private String email;
    private String mobileNumber;
    private Integer yearsOfExperience;
    private String address;

    private Double rating;

    private Boolean verified;

    private List<Long> truckIds;
}
