package com.petspa.loyalty_service.service;

import com.petspa.loyalty_service.entity.LoyaltyPoint;

public interface LoyaltyPointService {

    Integer getPointsByUserId(String userId);

    void calculateDailyPoints();

    LoyaltyPoint getLatestPointRecord(String userId);
}
