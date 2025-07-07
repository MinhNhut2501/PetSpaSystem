package com.petspa.booking_service.config;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BookingDocument implements Serializable {

    private String bookingId;
    private String userId;
    private String userName;
    private String petId;
    private String petName;
    private LocalDateTime bookingTime;
    private String status;
    private BigDecimal totalAmount;

    private List<ServiceItem> services;

    @Data
    @Builder
    public static class ServiceItem implements Serializable {
        private String id;
        private String name;
        private BigDecimal price;
        private Integer duration;
    }
}
