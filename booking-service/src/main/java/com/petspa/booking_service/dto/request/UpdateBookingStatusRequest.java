package com.petspa.booking_service.dto.request;

import com.petspa.booking_service.enumration.BookingStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateBookingStatusRequest {
    private String status;
}
