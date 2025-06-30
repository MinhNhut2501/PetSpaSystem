package com.petspa.notification_service.listener;
import com.petspa.common_service.dto.NotificationMessage;
import com.petspa.notification_service.service.Impl.EmailServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final EmailServiceImpl emailService;

    @RabbitListener(queues = "${spring.rabbitmq.template.default-receive-queue}")
    public void handleNotification(NotificationMessage message) {
        log.info("Received notification for email: {}, subject: {}, content: {}",
                message.getEmail(), message.getSubject(), message.getActivationLink());
        emailService.sendEmail(message);
    }
}