package com.petspa.booking_service.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateBookingRequest {
    String userId;
    String petId;
    LocalDateTime bookingTime;
    List<ServiceItem> services;

    @Data
    public static class ServiceItem {
        private String serviceId;
        private String name;
        private BigDecimal price;
        private Integer duration;
    }
}
