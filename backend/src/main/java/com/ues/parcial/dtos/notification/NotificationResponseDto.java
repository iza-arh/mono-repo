package com.ues.parcial.dtos.notification;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

import com.ues.parcial.Models.Enums.NotificationChannel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponseDto {
    private UUID id;
    private String userId;
    private NotificationChannel notificationChannel;
    private Map<String, Object> payload;
    private String status;
    private OffsetDateTime createdAt;
}
