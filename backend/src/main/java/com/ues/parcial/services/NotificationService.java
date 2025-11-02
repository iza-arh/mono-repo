package com.ues.parcial.services;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ues.parcial.Models.Notification;
import com.ues.parcial.Models.User;
import com.ues.parcial.Models.Enums.NotificationChannel;
import com.ues.parcial.dtos.notification.BulkNotificationResult;
import com.ues.parcial.dtos.notification.CreateMultiNotificationsDto;
import com.ues.parcial.dtos.notification.CreateNotificationDto;
import com.ues.parcial.dtos.notification.NotificationFailed;
import com.ues.parcial.dtos.notification.NotificationResponseDto;
import com.ues.parcial.exceptions.NotificationSendException;
import com.ues.parcial.exceptions.ResourceNotFoundException;
import com.ues.parcial.repositories.NotificationRepository;
import com.ues.parcial.repositories.UserRepository;
import com.ues.parcial.services.notification.senders.NotificationSender;

@Service
public class NotificationService {
    
    // Status constants
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_SENT = "SENT";
    public static final String STATUS_FAILED = "FAILED";
    public static final String STATUS_READ = "READ";

    private final Map<NotificationChannel, NotificationSender> senders;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper; 

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository, ObjectMapper objectMapper, List<NotificationSender> senderList) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;

        // We converted the list into a map for quick access by channel
        this.senders = senderList.stream().collect(Collectors.toMap(
                NotificationSender::getChannelType,
                Function.identity()
            ));
    }

    // Helper method to create Notification.
    private Notification createNotificationEntity(String userId, NotificationChannel channel, Object payload) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Notification n = new Notification();
        n.setUser(user);
        n.setNotificationChannel(channel);
        n.setPayload(objectMapper.valueToTree(payload));
        n.setStatus(STATUS_PENDING);
        return notificationRepository.save(n);
    }

    // Overloaded helper for single notification
    private Notification createNotificationEntity(CreateNotificationDto dto) {
        return createNotificationEntity(dto.getUserId(), dto.getChannel(), dto.getPayload());
    }

    // Overloaded helper for multi notifications
    private Notification createNotificationEntity(String userId, CreateMultiNotificationsDto dto) {
        return createNotificationEntity(userId, dto.getChannel(), dto.getPayload());
    }

    // Helper method to send notification based on its channel
    private void sendNotification(Notification notification) {
        NotificationSender sender = senders.get(notification.getNotificationChannel());
        
        if (sender == null) {
            notification.setStatus(STATUS_FAILED); // If no sender found, mark as failed
            notificationRepository.save(notification);
            throw new UnsupportedOperationException("Unsupported notification channel: " + notification.getNotificationChannel());
        }

        try {
            sender.send(notification); // Attempt to send the notification based on its channel
            notification.setStatus(STATUS_SENT);
        } catch (Exception e) {
            notification.setStatus(STATUS_FAILED);
            throw new NotificationSendException("Error sending notification", e);
        } finally {
            notificationRepository.save(notification);
        }
    }

    @Transactional
    public Notification createNotification(CreateNotificationDto dto) {
        Notification notification = createNotificationEntity(dto);

        // If sendNotification flag is true, send the notification immediately
        if (dto.isSendNotification()) {
            sendNotification(notification);
        }
        return notification;
    }

    @Transactional
    public BulkNotificationResult createAndSendNotifications(CreateMultiNotificationsDto dto) {
        List<User> users = userRepository.findAllById(dto.getUserIds());
        List<Notification> successful = new ArrayList<>();
        List<NotificationFailed> failed = new ArrayList<>();

        for (User user : users) {
            try {
                Notification notification = createNotificationEntity(user.getId(), dto);
                sendNotification(notification);
                successful.add(notification);
                
            } catch (Exception e) {
                failed.add(new NotificationFailed(user.getId(), e.getMessage()));
            }
        }

        // Convert successful notifications to DTOs
        List<NotificationResponseDto> successfulDtos = successful.stream()
            .map(this::toResponseDto)
            .toList();

        return new BulkNotificationResult(successfulDtos, failed); // Return both successful and failed lists
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