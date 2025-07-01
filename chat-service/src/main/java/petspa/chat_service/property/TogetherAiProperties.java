package petspa.chat_service.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "together")
public class TogetherAiProperties {
    private String apiUrl;
    private String apiKey;
    private String model;
}
