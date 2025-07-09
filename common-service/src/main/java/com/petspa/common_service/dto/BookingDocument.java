package com.petspa.common_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "booking_documents")
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
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ServiceItem implements Serializable {
        private String id;
        private String name;
        private BigDecimal price;
        private Integer duration;
    }
}
