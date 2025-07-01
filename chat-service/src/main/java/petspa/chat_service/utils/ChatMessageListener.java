package petspa.chat_service.utils;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import petspa.chat_service.dto.ChatMessage;
import petspa.chat_service.service.AiProcessorService;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMessageListener {

    private final AiProcessorService aiProcessorService;

    @OnConnect
    public void onConnect(SocketIOClient client) {
        log.info("Client connected: {}", client.getSessionId());
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        log.info("Client disconnected: {}", client.getSessionId());
    }

    @OnEvent("chat_message")
    public void onChatMessage(SocketIOClient client, ChatMessage chatMessage) {
        log.info("📩 Received message: {}", chatMessage);

        if ("ai-bot".equals(chatMessage.getReceiverId())) {
            // Gọi AI để xử lý và phản hồi
            aiProcessorService.handle(chatMessage, client);
        } else {
            // Gửi lại tin cho người dùng (echo)
            client.sendEvent("chat_response", chatMessage);
        }
    }
}
