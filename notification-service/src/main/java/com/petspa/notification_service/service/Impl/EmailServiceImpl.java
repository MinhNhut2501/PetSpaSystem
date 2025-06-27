package com.petspa.notification_service.service.Impl;

import com.petspa.common_service.dto.NotificationMessage;
import com.petspa.notification_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(NotificationMessage message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(message.getEmail()); // Địa chỉ email người nhận từ NotificationMessage
        mailMessage.setSubject(message.getSubject()); // Tiêu đề email
        mailMessage.setText(message.getContent()); // Nội dung email
        try {
            mailSender.send(mailMessage);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
