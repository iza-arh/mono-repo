package com.ues.parcial.dtos.notification;

import java.util.List;
import java.util.Map;

import com.ues.parcial.Models.Enums.NotificationChannel;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateMultiNotificationsDto {

    @NotNull(message = "User IDs cannot be null")
    private List<String> userIds; 

    @NotNull(message = "Notification channel cannot be null")
    private NotificationChannel channel; 

    @NotNull(message = "Payload cannot be null")
    private Map<String, Object> payload;
}
