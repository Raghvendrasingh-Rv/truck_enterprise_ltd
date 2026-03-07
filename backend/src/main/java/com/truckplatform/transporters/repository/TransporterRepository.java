package com.truckplatform.transporters.repository;

import com.truckplatform.transporters.entity.Transporter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransporterRepository extends JpaRepository<Transporter, Long> {
    Optional<Transporter> findByUserId(Long userId);
    List<Transporter> findByVerified(Boolean verified);
    List<Transporter> findByRatingGreaterThanEqual(Double rating);
}
