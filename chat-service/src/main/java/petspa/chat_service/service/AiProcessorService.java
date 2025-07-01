package petspa.chat_service.service;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import petspa.chat_service.client.BookingClient;
import petspa.chat_service.dto.BookingInfo;
import petspa.chat_service.dto.ChatMessage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiProcessorService {
    private final BookingClient bookingClient;
    private final TogetherAiService togetherAiService;

    public void handle(ChatMessage chatMessage, SocketIOClient client) {
        String question = chatMessage.getMessage();
        String userId = chatMessage.getSenderId();
        log.info("🔍 Nhận câu hỏi gửi đến AI: {}", question);
        // logic xử lý câu hỏi thường
        if (question.toLowerCase().contains("spa") && question.contains("hôm nay")) {
            BookingInfo info = bookingClient.getBookingForToday(userId);
            String reply = (info != null)
                    ? "🐾 Bạn đã đặt lịch spa lúc " + info.getTime()
                    : "😿 Bạn chưa có lịch spa nào hôm nay.";
            client.sendEvent("chat_response", new ChatMessage("ai-bot", userId, reply, now()));
            return;
        }

        // gọi Together AI
        String aiReply = togetherAiService.ask(question, userId);
        client.sendEvent("chat_response", new ChatMessage("ai-bot", userId, aiReply, now()));
    }

    private String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
