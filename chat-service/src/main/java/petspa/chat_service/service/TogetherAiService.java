package petspa.chat_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import petspa.chat_service.client.UserSearchClient;
import petspa.chat_service.dto.UserSearchDocument;
import petspa.chat_service.property.TogetherAiProperties;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TogetherAiService {

    private final TogetherAiProperties props;
    private final RestTemplate restTemplate = new RestTemplate();
    private final UserSearchClient userSearchClient;

    public String ask(String userMessage, String userId) {
        String context = "";

        try {
            UserSearchDocument doc = userSearchClient.getUserContext(userId);
            context = "Thông tin người dùng:\n" +
                    "- Họ tên: " + doc.getFullName() + "\n" +
                    "- Số thú cưng: " + doc.getPetNames().size() + "\n" +
                    "- Tên thú cưng: " + String.join(", ", doc.getPetNames()) + "\n" +
                    "- Số lần đi spa: " + doc.getSpaCount() + "\n" +
                    "- Số điện thoại: " + doc.getPhone() + "\n" +
                    "- Lần cuối: " + doc.getLastVisit() + "\n";
        } catch (Exception e) {
            log.warn("Không lấy được context người dùng: {}", e.getMessage());
        }

        Map<String, Object> body = Map.of(
                "model", props.getModel(),
                "messages", List.of(
                        Map.of("role", "system", "content", "Bạn là trợ lý AI cho spa thú cưng. Hãy trả lời ngắn gọn, thân thiện."),
                        Map.of("role", "user", "content", context + "\nCâu hỏi: " + userMessage)
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(props.getApiKey());

        HttpEntity<?> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(props.getApiUrl(), HttpMethod.POST, request, Map.class);
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            return (String) message.get("content");
        } catch (Exception e) {
            log.error("Lỗi gọi Together AI: {}", e.getMessage());
            return "Xin lỗi, tôi không thể trả lời lúc này.";
        }
    }
}

