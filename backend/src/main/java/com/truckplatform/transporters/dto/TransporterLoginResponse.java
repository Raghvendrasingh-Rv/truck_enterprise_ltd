package com.truckplatform.transporters.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransporterLoginResponse {

    private String token;
    private Long transporterId;
    private String email;
    private String name;
    private String companyName;
    private String tokenType = "Bearer";
}
