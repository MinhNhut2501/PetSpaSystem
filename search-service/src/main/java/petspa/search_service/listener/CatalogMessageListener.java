package petspa.search_service.listener;

import com.petspa.common_service.dto.BookingSyncMessage;
import com.petspa.common_service.dto.CatalogSyncMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import petspa.search_service.service.BookingSearchService;
import petspa.search_service.service.CatalogSearchService;

@Slf4j
@Component
@RequiredArgsConstructor
public class CatalogMessageListener {

    private final CatalogSearchService catalogSearchService;

    @RabbitListener(queues = "catalog.elasticsearch.sync")
    public void handleBookingSync(CatalogSyncMessage message) {
        switch (message.getAction()) {
            case "UPSERT" -> catalogSearchService.upsertCatalog(message.getCatalog());
            case "DELETE" -> catalogSearchService.deleteCatlogById(message.getCatalog().getId());
            default -> log.warn("Unknown action: {}", message.getAction());
        }
    }

}
