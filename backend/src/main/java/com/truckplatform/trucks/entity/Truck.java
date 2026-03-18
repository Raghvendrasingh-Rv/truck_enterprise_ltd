package com.truckplatform.trucks.entity;

import com.truckplatform.transporters.entity.Transporter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trucks", uniqueConstraints = {
    @UniqueConstraint(columnNames = "truck_number")
})
@Getter
@Setter
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

    // Explicit getters and setters to bypass Lombok processing
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Transporter getTransporter() {
        return transporter;
    }

    public void setTransporter(Transporter transporter) {
        this.transporter = transporter;
    }

    public String getTruckNumber() {
        return truckNumber;
    }

    public void setTruckNumber(String truckNumber) {
        this.truckNumber = truckNumber;
    }

    public TruckType getTruckType() {
        return truckType;
    }

    public void setTruckType(TruckType truckType) {
        this.truckType = truckType;
    }

    public Long getCapacityKg() {
        return capacityKg;
    }

    public void setCapacityKg(Long capacityKg) {
        this.capacityKg = capacityKg;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }

    public TruckStatus getStatus() {
        return status;
    }

    public void setStatus(TruckStatus status) {
        this.status = status;
    }
}
