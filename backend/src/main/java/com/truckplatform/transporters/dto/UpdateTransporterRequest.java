package com.truckplatform.transporters.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTransporterRequest {

    private String companyName;
    private String name;

    @Email(message = "Email should be valid")
    private String email;

    @Size(min = 10, max = 15, message = "Mobile number must be between 10 and 15 digits")
    private String mobileNumber;

    @Min(value = 0, message = "Years of experience cannot be negative")
    private Integer yearsOfExperience;

    private String address;

    @Min(value = 0, message = "Rating cannot be negative")
    private Double rating;

    private Boolean verified;
}
