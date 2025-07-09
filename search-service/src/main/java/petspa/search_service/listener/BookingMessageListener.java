package petspa.search_service.listener;

import com.petspa.common_service.dto.BookingSyncMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import petspa.search_service.service.BookingSearchService;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingMessageListener {

    private final BookingSearchService bookingSearchService;

    @RabbitListener(queues = "booking.elasticsearch.sync")
    public void handleBookingSync(BookingSyncMessage message) {
        switch (message.getAction()) {
            case "UPSERT" -> bookingSearchService.upsertBooking(message.getBooking());
            case "DELETE" -> bookingSearchService.deleteBookingById(message.getBooking().getBookingId());
            default -> log.warn("Unknown action: {}", message.getAction());
        }
    }
}
