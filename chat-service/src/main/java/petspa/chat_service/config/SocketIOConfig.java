package petspa.chat_service.config;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketIOConfig {

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname("0.0.0.0");
        config.setPort(9092); // hoặc bất kỳ cổng nào bạn chọn
        config.setOrigin("*"); // Cho phép tất cả các nguồn gốc, bạn có thể điều chỉnh theo nhu cầu bảo mật

        return new SocketIOServer(config);
    }
}