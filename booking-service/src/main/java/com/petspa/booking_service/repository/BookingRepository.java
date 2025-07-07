package com.petspa.booking_service.repository;

import com.petspa.booking_service.entity.Booking;
import com.petspa.booking_service.enumration.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
    List<Booking> findByUserId(String userId);
    List<Booking> findByStatus(BookingStatus status);
    List<Booking> findByBookingTimeBetween(LocalDateTime from, LocalDateTime to);
}
