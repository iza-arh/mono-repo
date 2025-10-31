package com.ues.parcial.dtos.notification;

import java.util.Map;

import com.ues.parcial.Models.Enums.NotificationChannel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateNotificationDto {

    @NotBlank(message = "userId is required")
    private String userId;

    @NotNull(message = "channel is required")
    private NotificationChannel channel;

    @NotNull(message = "payload is required")
    private Map<String, Object> payload;
}
