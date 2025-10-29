package com.ues.parcial.controllers;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ues.parcial.Models.Enums.NotificationChannel;
import com.ues.parcial.dtos.notification.CreateNotificationDto;
import com.ues.parcial.dtos.notification.UpdateNotificationStatusDto;
import com.ues.parcial.services.NotificationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<?> createNotification(@Valid @RequestBody CreateNotificationDto dto) {
        return ResponseEntity.ok(notificationService.toResponseDto(notificationService.createNotification(dto)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateNotificationStatus(@PathVariable UUID id, @Valid @RequestBody UpdateNotificationStatusDto dto) {
        return ResponseEntity.ok(notificationService.toResponseDto(notificationService.updateStatus(id, dto.getStatus())));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable UUID id) {
        return ResponseEntity.ok(notificationService.toResponseDto(notificationService.markAsRead(id)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getNotificationsForUser(@PathVariable String userId) {
        return ResponseEntity.ok(notificationService.toResponseDto(notificationService.getNotificationsForUser(userId)));
    }

    // Get notifications by status. Example: /api/notifications/status/READ
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(notificationService.toResponseDto(notificationService.getNotificationsByStatus(status)));
    }

    // Get notifications for a user by channel. Example: /api/notifications/user/{userId}/channel/EMAIL
    @GetMapping("/user/{userId}/channel/{channel}")
    public ResponseEntity<?> getByUserAndChannel(@PathVariable String userId, @PathVariable NotificationChannel channel) {
        return ResponseEntity.ok(notificationService.toResponseDto(notificationService.getNotificationsForUserByChannel(userId, channel)));
    }

    // Get notifications created between two dates. Example: /api/notifications/range?start=2025-10-01T00:00:00Z&end=2025-10-31T23:59:59Z
    @GetMapping("/range")
    public ResponseEntity<?> getBetween(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime start,
                                        @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime end) {
        return ResponseEntity.ok(notificationService.toResponseDto(notificationService.getNotificationsBetween(start, end)));
    }

    // Count notifications for a specific user. Example: /api/notifications/count/user/{userId}
    @GetMapping("/count/user/{userId}")
    public ResponseEntity<?> countForUser(@PathVariable String userId) {
        return ResponseEntity.ok(notificationService.countForUser(userId));
    }

    // Count notifications by status. Example: /api/notifications/count/status/READ
    @GetMapping("/count/status/{status}")
    public ResponseEntity<?> countByStatus(@PathVariable String status) {
        return ResponseEntity.ok(notificationService.countByStatus(status));
    }

    // Delete notifications by status. Example: /api/notifications/status/READ
    @DeleteMapping("/status/{status}")
    public ResponseEntity<?> deleteByStatus(@PathVariable String status) {
        notificationService.deleteByStatus(status);
        return ResponseEntity.noContent().build();
    }

    // Delete notifications created before a certain date. Example: /api/notifications/before?cutoff=2025-10-29T00:00:00Z
    @DeleteMapping("/before")
    public ResponseEntity<?> deleteBefore(@RequestParam("cutoff") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime cutoff) {
        notificationService.deleteBefore(cutoff);
        return ResponseEntity.noContent().build();
    }

    // Delete notifications for a specific user
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> deleteByUser(@PathVariable String userId) {
        notificationService.deleteByUserId(userId);
        return ResponseEntity.noContent().build();
    }
}