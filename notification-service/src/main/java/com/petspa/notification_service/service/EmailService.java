package com.petspa.notification_service.service;

import com.petspa.common_service.dto.NotificationMessage;

public interface EmailService {
    void sendEmail(NotificationMessage message);
}
