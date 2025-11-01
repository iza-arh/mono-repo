package com.ues.parcial.services.notification.senders;

import org.springframework.stereotype.Service;

import com.ues.parcial.Models.Notification;
import com.ues.parcial.Models.Enums.NotificationChannel;
import com.ues.parcial.services.EmailService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailNotificationSender implements NotificationSender {
    private final EmailService emailService;

    @Override
    public void send(Notification notification) throws MessagingException {
        emailService.sendNotificationEmail(notification);
    }

    @Override
    public NotificationChannel getChannelType() {
        return NotificationChannel.EMAIL;
    }
}
