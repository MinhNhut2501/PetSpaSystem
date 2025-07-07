package com.petspa.booking_service.mapper;

import com.petspa.booking_service.dto.response.BookingResponse;
import com.petspa.booking_service.dto.response.BookingServiceDetailResponse;
import com.petspa.booking_service.entity.Booking;
import com.petspa.booking_service.entity.BookingServiceDetail;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    BookingResponse toBookingResponse(Booking booking);
    List<BookingResponse> toBookingResponseList(List<Booking> bookings);
    BookingServiceDetailResponse toBookingServiceDetailResponse(BookingServiceDetail detail);
    List<BookingServiceDetailResponse> toBookingServiceDetailResponseList(List<BookingServiceDetail> details);

}
