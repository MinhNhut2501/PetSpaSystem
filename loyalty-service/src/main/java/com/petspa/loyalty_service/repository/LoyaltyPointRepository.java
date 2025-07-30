package com.petspa.loyalty_service.repository;

import com.petspa.loyalty_service.entity.LoyaltyPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LoyaltyPointRepository extends JpaRepository<LoyaltyPoint, String> {
    Optional<LoyaltyPoint> findTopByUserIdOrderByCreatedDateDesc(String userId);

    @Query("SELECT SUM(lp.points) FROM LoyaltyPoint lp WHERE lp.userId = :userId")
    Integer getTotalPointsByUserId(String userId);
}
