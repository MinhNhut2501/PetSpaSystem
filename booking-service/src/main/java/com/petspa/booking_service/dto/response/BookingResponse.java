package com.petspa.booking_service.dto.response;

import com.petspa.booking_service.enumration.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {
    private String id;
    private String userId;
    private String petId;
    private LocalDateTime bookingTime;
    private BookingStatus status;
    private BigDecimal totalAmount;
    private List<BookingServiceDetailResponse> services;
}


