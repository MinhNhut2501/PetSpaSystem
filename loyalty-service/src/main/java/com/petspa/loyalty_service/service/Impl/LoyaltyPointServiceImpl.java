package com.petspa.loyalty_service.service.Impl;

import com.petspa.loyalty_service.FeignClient.BookingServiceClient;
import com.petspa.loyalty_service.entity.LoyaltyPoint;
import com.petspa.loyalty_service.repository.LoyaltyPointRepository;
import com.petspa.loyalty_service.service.LoyaltyPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoyaltyPointServiceImpl implements LoyaltyPointService {

    private final BookingServiceClient bookingServiceClient;
    private final LoyaltyPointRepository loyaltyPointRepository;
    private final RedisTemplate<String, Integer> redisTemplate;

    @Override
    public Integer getPointsByUserId(String userId) {
        String redisKey = "loyalty:points:" + userId;

        Integer cachedPoints = redisTemplate.opsForValue().get(redisKey);
        if (cachedPoints != null) {
            return cachedPoints;
        }

        Integer totalPoints = loyaltyPointRepository.getTotalPointsByUserId(userId);
        return totalPoints != null ? totalPoints : 0;
    }

    @Override
    @Transactional
    public void calculateDailyPoints() {
        // 1. Gọi BookingService lấy tổng chi tiêu hôm qua
        Map<String, Long> spendingMap = bookingServiceClient.getTotalSpendingYesterday();

        LocalDate today = LocalDate.now();

        for (Map.Entry<String, Long> entry : spendingMap.entrySet()) {
            String userId = entry.getKey();
            Long totalSpent = entry.getValue();

            int points = (int) (totalSpent / 10_000);

            // 2. Lưu LoyaltyPoint vào DB
            LoyaltyPoint loyaltyPoint = LoyaltyPoint.builder()
                    .userId(userId)
                    .totalSpent(totalSpent)
                    .points(points)
                    .createdDate(today.minusDays(1)) // điểm cho ngày hôm qua
                    .build();
            loyaltyPointRepository.save(loyaltyPoint);

            // 3. Cache điểm mới vào Redis: key = loyalty:points:{userId}
            String redisKey = "loyalty:points:" + userId;
            redisTemplate.opsForValue().set(redisKey, points);
        }
    }

    @Override
    public LoyaltyPoint getLatestPointRecord(String userId) {
        return loyaltyPointRepository.findTopByUserIdOrderByCreatedDateDesc(userId)
                .orElse(null);
    }

}


