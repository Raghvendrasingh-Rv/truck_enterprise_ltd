package com.truckplatform.trucks.entity;

import com.truckplatform.transporters.entity.Transporter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trucks", uniqueConstraints = {
    @UniqueConstraint(columnNames = "truck_number")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Truck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transporter_id", nullable = false)
    private Transporter transporter;

    @NotBlank(message = "Truck number is required")
    @Column(nullable = false, unique = true)
    private String truckNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TruckType truckType;

    @Min(value = 0, message = "Capacity must be positive")
    @Column(nullable = false)
    private Long capacityKg;

    @NotBlank(message = "Location city is required")
    @Column(nullable = false)
    private String locationCity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TruckStatus status = TruckStatus.AVAILABLE;
}
