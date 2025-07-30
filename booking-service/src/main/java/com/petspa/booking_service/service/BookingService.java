package com.petspa.booking_service.service;

import com.petspa.booking_service.dto.request.CreateBookingRequest;
import com.petspa.booking_service.dto.response.BookingResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface BookingService {
    BookingResponse createBooking(CreateBookingRequest request);
    BookingResponse getBooking(String id);
    List<BookingResponse> getBookingsByUser(String userId);
    List<BookingResponse> getBookingsByStatus(String status);
    List<BookingResponse> getBookingsInTimeRange(LocalDateTime from, LocalDateTime to);
    BookingResponse updateStatus(String bookingId, String status);
    void deleteBooking(String bookingId);
    Map<String, Long> getTotalSpentYesterday();
}
