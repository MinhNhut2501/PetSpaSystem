package com.petspa.notification_service.service.Impl;

import com.petspa.common_service.dto.NotificationMessage;
import com.petspa.notification_service.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Override
    public void sendEmail(NotificationMessage message) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            helper.setTo(message.getEmail());
            helper.setSubject(message.getSubject());

            // Dữ liệu để render vào HTML
            Context context = new Context();
            context.setVariable("fullName", message.getFullName());  // Bạn cần bổ sung vào NotificationMessage
            context.setVariable("activationLink",  message.getActivationLink());

            String htmlContent = templateEngine.process("verify-email.html", context);

            helper.setText(htmlContent, true); // true: isHtml

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("Failed to send email", e);
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
