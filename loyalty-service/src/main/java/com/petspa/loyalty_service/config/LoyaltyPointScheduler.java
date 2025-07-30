package com.petspa.loyalty_service.config;
import com.petspa.loyalty_service.service.LoyaltyPointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
@Slf4j
public class LoyaltyPointScheduler {

    private final LoyaltyPointService loyaltyPointService;

    /**
     * Chạy tự động mỗi ngày lúc 00:05.
     * Tính điểm thưởng mới cho tất cả khách hàng dựa trên tổng chi tiêu hôm qua.
     * Kết quả sẽ được lưu DB và đẩy lên Redis.
     */
    @Scheduled(cron = "0 5 0 * * *")
    public void runDailyPointCalculation() {
        log.info("🔄 [Scheduler] Bắt đầu tính điểm thưởng hàng ngày...");

        loyaltyPointService.calculateDailyPoints();

        log.info("✅ [Scheduler] Tính điểm thưởng xong!");
    }
}