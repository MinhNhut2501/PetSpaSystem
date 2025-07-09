package petspa.search_service.service;

import com.petspa.common_service.dto.BookingDocument;

public interface BookingSearchService {
    void upsertBooking(BookingDocument booking);
    void deleteBookingById(String bookingId);
}
