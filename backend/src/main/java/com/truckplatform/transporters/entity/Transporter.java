package com.truckplatform.transporters.entity;

import com.truckplatform.trucks.entity.Truck;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "transporters", uniqueConstraints = {
    @UniqueConstraint(columnNames = "email"),
    @UniqueConstraint(columnNames = "mobile_number")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transporter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Company name is required")
    @Column(nullable = false)
    private String companyName;

    @NotBlank(message = "Transporter name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Mobile number is required")
    @Column(name = "mobile_number", nullable = false, unique = true)
    private String mobileNumber;

    @Min(value = 0, message = "Years of experience cannot be negative")
    @Column(nullable = false)
    private Integer yearsOfExperience;

    @NotBlank(message = "Address is required")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;

    @Column(nullable = false)
    private String passwordHash;

    @Min(value = 0, message = "Rating cannot be negative")
    @Column(nullable = false)
    private Double rating = 0.0;

    @Column(nullable = false)
    private Boolean verified = false;

    @OneToMany(mappedBy = "transporter", fetch = FetchType.LAZY)
    private List<Truck> trucks = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
    public Integer getYearsOfExperience() { return yearsOfExperience; }
    public void setYearsOfExperience(Integer yearsOfExperience) { this.yearsOfExperience = yearsOfExperience; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
    public Boolean getVerified() { return verified; }
    public void setVerified(Boolean verified) { this.verified = verified; }
    public List<Truck> getTrucks() { return trucks; }
    public void setTrucks(List<Truck> trucks) { this.trucks = trucks; }
}
