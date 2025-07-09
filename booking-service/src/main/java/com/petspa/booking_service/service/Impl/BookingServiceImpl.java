package com.petspa.booking_service.service.Impl;

import com.petspa.booking_service.config.BookingProducer;
import com.petspa.booking_service.dto.request.CreateBookingRequest;
import com.petspa.booking_service.dto.response.BookingResponse;
import com.petspa.booking_service.entity.Booking;
import com.petspa.booking_service.entity.BookingServiceDetail;
import com.petspa.booking_service.enumration.BookingStatus;
import com.petspa.booking_service.mapper.BookingMapper;
import com.petspa.booking_service.repository.BookingRepository;
import com.petspa.booking_service.service.BookingService;
import com.petspa.common_service.dto.BookingDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingProducer bookingProducer;
    private final BookingMapper bookingMapper;

    @Override
    public BookingResponse createBooking(CreateBookingRequest request) {
        List<BookingServiceDetail> details = request.getServices().stream()
                .map(svc -> BookingServiceDetail.builder()
                        .serviceId(svc.getServiceId())
                        .name(svc.getName())
                        .price(svc.getPrice())
                        .duration(svc.getDuration())
                        .build())
                .collect(Collectors.toList());

        BigDecimal total = details.stream()
                .map(BookingServiceDetail::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Booking booking = Booking.builder()
                .userId(request.getUserId())
                .petId(request.getPetId())
                .bookingTime(request.getBookingTime())
                .status(BookingStatus.PENDING) // enum thay cho String
                .totalAmount(total)
                .services(details)
                .build();

        // Gán quan hệ ngược để JPA persist đúng
        details.forEach(detail -> detail.setBooking(booking));

        bookingRepository.save(booking);

        BookingDocument doc = buildBookingDocument(booking);
        bookingProducer.sendCreateOrUpdateMessage(doc);

        return bookingMapper.toBookingResponse(booking);
    }

    @Override
    public BookingResponse getBooking(String id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        return bookingMapper.toBookingResponse(booking);
    }

    @Override
    public List<BookingResponse> getBookingsByUser(String userId) {
        List<Booking> bookings = bookingRepository.findByUserId(userId);
        return bookingMapper.toBookingResponseList(bookings);
    }

    @Override
    public List<BookingResponse> getBookingsByStatus(String status) {
        List<Booking> bookings = bookingRepository.findByStatus(BookingStatus.valueOf(status));
        return bookingMapper.toBookingResponseList(bookings);
    }

    @Override
    public List<BookingResponse> getBookingsInTimeRange(LocalDateTime from, LocalDateTime to) {
        List<Booking> bookings = bookingRepository.findByBookingTimeBetween(from, to);
        return bookingMapper.toBookingResponseList(bookings);
    }

    @Override
    public BookingResponse updateStatus(String bookingId, String status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(BookingStatus.valueOf(status));
        bookingRepository.save(booking);
        bookingProducer.sendCreateOrUpdateMessage(buildBookingDocument(booking));
        return bookingMapper.toBookingResponse(booking);
    }

    @Override
    public void deleteBooking(String bookingId) {
        bookingRepository.deleteById(bookingId);
        bookingProducer.sendDeleteMessage(bookingId);
    }

    private BookingDocument buildBookingDocument(Booking booking) {
        return BookingDocument.builder()
                .bookingId(booking.getId())
                .userId(booking.getUserId())
                .petId(booking.getPetId())
                .bookingTime(booking.getBookingTime())
                .status(booking.getStatus().name())
                .userName("") // TODO: gọi user-service nếu cần
                .petName("")  // TODO: gọi pet-service nếu cần
                .totalAmount(booking.getTotalAmount())
                .services(booking.getServices().stream()
                        .map(svc -> BookingDocument.ServiceItem.builder()
                                .id(svc.getServiceId())
                                .name(svc.getName())
                                .price(svc.getPrice())
                                .duration(svc.getDuration())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}

