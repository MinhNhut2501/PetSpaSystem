package com.petspa.booking_service.config;

import com.petspa.common_service.dto.BookingDocument;
import com.petspa.common_service.dto.BookingSyncMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingProducer {

    private final RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE = "booking.exchange";
    private static final String ROUTING_KEY = "booking.sync";

    public void sendCreateOrUpdateMessage(BookingDocument document) {
        BookingSyncMessage message = BookingSyncMessage.builder()
                .action("UPSERT") // hoặc "CREATE"/"UPDATE" nếu muốn tách
                .booking(document)
                .build();
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, message);
    }

    public void sendDeleteMessage(String bookingId) {
        BookingSyncMessage message = BookingSyncMessage.builder()
                .action("DELETE")
                .booking(BookingDocument.builder().bookingId(bookingId).build())
                .build();
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, message);
    }
}