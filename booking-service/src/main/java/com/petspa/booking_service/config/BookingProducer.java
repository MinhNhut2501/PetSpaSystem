package com.petspa.booking_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendBookingToIndex(BookingDocument document) {
        rabbitTemplate.convertAndSend("booking.index.exchange", "booking.index.routing", document);
    }

    public void deleteBookingFromIndex(String bookingId) {
        rabbitTemplate.convertAndSend("booking.delete.exchange", "booking.delete.routing", bookingId);
    }
}