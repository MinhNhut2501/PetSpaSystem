package petspa.search_service.kafka;

import com.petspa.common_service.dto.BookingSyncMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import petspa.search_service.service.BookingSearchService;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingConsumer {
    private final BookingSearchService bookingSearchService;

    @KafkaListener(topics = "booking-events", groupId = "search-service")
    public void handleBookingSync(BookingSyncMessage message) {
        switch (message.getAction()) {
            case "UPSERT" -> bookingSearchService.upsertBooking(message.getBooking());
            case "DELETE" -> bookingSearchService.deleteBookingById(message.getBooking().getBookingId());
            default -> log.warn("Unknown action: {}", message.getAction());
        }
    }
}
