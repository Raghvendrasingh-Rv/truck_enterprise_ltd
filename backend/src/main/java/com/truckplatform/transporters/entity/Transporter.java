package com.truckplatform.transporters.entity;

import com.truckplatform.users.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transporters")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transporter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @NotBlank(message = "Company name is required")
    @Column(nullable = false)
    private String companyName;

    @Min(value = 0, message = "Rating cannot be negative")
    @Column(nullable = false)
    private Double rating = 0.0;

    @Column(nullable = false)
    private Boolean verified = false;
}
