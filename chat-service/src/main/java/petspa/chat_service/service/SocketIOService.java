package petspa.chat_service.service;

import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import petspa.chat_service.utils.ChatMessageListener;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SocketIOService {
    SocketIOServer socketIOServer;
    ChatMessageListener chatMessageListener;
    @PostConstruct
    public void startServer() {
        socketIOServer.addListeners(chatMessageListener); // 🔥 Quan trọng
        socketIOServer.start();
    }

    @PreDestroy
    public void stopServer() {
        socketIOServer.stop();
    }
}
