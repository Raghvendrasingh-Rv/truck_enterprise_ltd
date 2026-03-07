package com.truckplatform.trucks.repository;

import com.truckplatform.trucks.entity.Truck;
import com.truckplatform.trucks.entity.TruckStatus;
import com.truckplatform.trucks.entity.TruckType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TruckRepository extends JpaRepository<Truck, Long> {
    Optional<Truck> findByTruckNumber(String truckNumber);
    List<Truck> findByTransporterId(Long transporterId);
    List<Truck> findByStatus(TruckStatus status);
    List<Truck> findByLocationCity(String locationCity);
    List<Truck> findByTruckType(TruckType truckType);
    List<Truck> findByLocationCityAndStatus(String locationCity, TruckStatus status);
    List<Truck> findByCapacityKgGreaterThanEqual(Long capacity);
}
