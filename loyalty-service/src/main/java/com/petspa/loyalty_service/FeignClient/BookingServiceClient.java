package com.petspa.loyalty_service.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@FeignClient(name = "booking-service", url = "${booking-service.url}")
public interface BookingServiceClient {

    @GetMapping("/booking/total-spent-yesterday")
    Map<String, Long> getTotalSpendingYesterday();

}
