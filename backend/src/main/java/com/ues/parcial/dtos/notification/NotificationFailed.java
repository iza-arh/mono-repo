package com.ues.parcial.dtos.notification;

public record NotificationFailed(
    String userId,
    String error
) {}