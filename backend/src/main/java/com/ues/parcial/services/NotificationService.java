package com.ues.parcial.services;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ues.parcial.Models.Notification;
import com.ues.parcial.Models.User;
import com.ues.parcial.Models.Enums.NotificationChannel;
import com.ues.parcial.dtos.notification.CreateNotificationDto;
import com.ues.parcial.dtos.notification.NotificationResponseDto;
import com.ues.parcial.exceptions.ResourceNotFoundException;
import com.ues.parcial.repositories.NotificationRepository;
import com.ues.parcial.repositories.UserRepository;

@Service
public class NotificationService {
    
    // Status constants
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_SENT = "SENT";
    public static final String STATUS_FAILED = "FAILED";
    public static final String STATUS_READ = "READ";

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper; 

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository, ObjectMapper objectMapper) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public Notification createNotification(CreateNotificationDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));

        Notification n = new Notification();
        n.setUser(user);
        n.setNotificationChannel(dto.getChannel());
        n.setPayload(objectMapper.valueToTree(dto.getPayload())); // Convert Map to JsonNode
        n.setStatus(STATUS_PENDING); // Default status

        return notificationRepository.save(n);
    }

    @Transactional
    public Notification updateStatus(UUID notificationId, String newStatus) {
        Notification n = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found: " + notificationId));

        n.setStatus(newStatus.toUpperCase());
        return notificationRepository.save(n);
    }

    @Transactional
    public Notification markAsRead(UUID notificationId) {
        return updateStatus(notificationId, STATUS_READ);
    }

    @Transactional(readOnly = true)
    public List<Notification> getNotificationsForUser(String userId) {
        return notificationRepository.findByUser_Id(userId);
    }

    @Transactional(readOnly = true)
    public List<Notification> getNotificationsByStatus(String status) {
        return notificationRepository.findByStatusIgnoreCase(status);
    }

    @Transactional(readOnly = true)
    public List<Notification> getNotificationsForUserByStatus(String userId, String status) {
        return notificationRepository.findByUser_IdAndStatusIgnoreCase(userId, status);
    }

    @Transactional(readOnly = true)
    public List<Notification> getNotificationsForUserByChannel(String userId, NotificationChannel channel) {
        return notificationRepository.findByUser_IdAndNotificationChannel(userId, channel);
    }

    @Transactional(readOnly = true)
    public List<Notification> getNotificationsBetween(OffsetDateTime start, OffsetDateTime end) {
        return notificationRepository.findByCreatedAtBetween(start, end);
    }

    @Transactional(readOnly = true)
    public long countForUser(String userId) {
        return notificationRepository.countByUser_Id(userId);
    }

    @Transactional(readOnly = true)
    public long countByStatus(String status) {
        return notificationRepository.countByStatusIgnoreCase(status);
    }

    @Transactional(readOnly = true)
    public long countByChannel(NotificationChannel channel) {
        return notificationRepository.countByNotificationChannel(channel);
    }

    @Transactional
    public void deleteByStatus(String status) {
        notificationRepository.deleteByStatusIgnoreCase(status);
    }

    @Transactional
    public void deleteBefore(OffsetDateTime cutoff) {
        notificationRepository.deleteByCreatedAtBefore(cutoff);
    }

    @Transactional
    public void deleteByUserId(String userId) {
        notificationRepository.deleteByUser_Id(userId);
    }

    public NotificationResponseDto toResponseDto(Notification n) {
        NotificationResponseDto dto = new NotificationResponseDto();

        // Convert JsonNode payload to Map
        if (n.getPayload() != null && !n.getPayload().isNull()) {
            Map<String, Object> payloadMap = objectMapper.convertValue(n.getPayload(), new TypeReference<Map<String, Object>>() {});
            dto.setPayload(payloadMap);
        }

        dto.setId(n.getId());
        dto.setUserId(n.getUser().getId());
        dto.setNotificationChannel(n.getNotificationChannel());
        dto.setStatus(n.getStatus());
        dto.setCreatedAt(n.getCreatedAt());
        return dto;
    }

    public List<NotificationResponseDto> toResponseDto(List<Notification> notifications) {
        return notifications.stream().map(this::toResponseDto).toList();
    }
}
