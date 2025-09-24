package com.petspa.common_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingSyncMessage {
    private String action; // CREATE, UPDATE, DELETE
    private BookingDocument booking; // null nếu action là DELETE
    private String traceId;
}
