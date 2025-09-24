package petspa.search_service.kafka;

import com.petspa.common_service.dto.BookingSyncMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
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
        MDC.put("traceId", message.getTraceId());
        try {
            log.info("Received booking log: {}", message);
            switch (message.getAction()) {
                case "UPSERT" -> bookingSearchService.upsertBooking(message.getBooking());
                case "DELETE" -> bookingSearchService.deleteBookingById(message.getBooking().getBookingId());
                default -> log.warn("Unknown action: {}", message.getAction());

            }
            log.info("Indexed booking log into Elasticsearch: bookingId={}", message.getBooking().getBookingId());
        } catch (Exception e) {
            log.error("Failed to index booking log", e);
        } finally {
            MDC.clear();
        }
    }
}
