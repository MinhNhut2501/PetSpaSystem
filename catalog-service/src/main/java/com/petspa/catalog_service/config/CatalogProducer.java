package com.petspa.catalog_service.config;

import com.petspa.common_service.dto.CatalogDocument;
import com.petspa.common_service.dto.CatalogSyncMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
public class CatalogProducer {
    private final RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE = "catalog.exchange";
    private static final String ROUTING_KEY = "catalog.sync";

    public void sendCreateOrUpdateMessage(CatalogDocument document) {
        CatalogSyncMessage message = CatalogSyncMessage.builder()
                .action("UPSERT") // hoặc "CREATE"/"UPDATE" nếu muốn tách
                .catalog(document)
                .build();
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, message);
    }

    public void sendDeleteMessage(String catalogId) {
        CatalogSyncMessage message = CatalogSyncMessage.builder()
                .action("DELETE")
                .catalog(CatalogDocument.builder().id(catalogId).build())
                .build();
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, message);
    }
}




