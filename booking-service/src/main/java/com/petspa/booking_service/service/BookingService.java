package com.petspa.booking_service.service;

import com.petspa.booking_service.dto.request.CreateBookingRequest;
import com.petspa.booking_service.dto.response.BookingResponse;
import com.petspa.booking_service.entity.Booking;
import com.petspa.booking_service.enumration.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    BookingResponse createBooking(CreateBookingRequest request);
    BookingResponse getBooking(String id);
    List<BookingResponse> getBookingsByUser(String userId);
    List<BookingResponse> getBookingsByStatus(String status);
    List<BookingResponse> getBookingsInTimeRange(LocalDateTime from, LocalDateTime to);
    BookingResponse updateStatus(String bookingId, String status);
    void deleteBooking(String bookingId);
}
