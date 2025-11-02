package com.ues.parcial.dtos.notification;

import java.util.List;

public record BulkNotificationResult(
    List<NotificationResponseDto> successful,
    List<NotificationFailed> failed
) {}
