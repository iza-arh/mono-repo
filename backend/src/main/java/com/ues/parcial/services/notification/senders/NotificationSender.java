package com.ues.parcial.services.notification.senders;

import com.ues.parcial.Models.Notification;
import com.ues.parcial.Models.Enums.NotificationChannel;

public interface NotificationSender {
    void send(Notification notification) throws Exception;
    NotificationChannel getChannelType();
}
