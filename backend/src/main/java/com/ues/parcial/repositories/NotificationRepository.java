package com.ues.parcial.repositories;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ues.parcial.Models.Notification;
import com.ues.parcial.Models.Enums.NotificationChannel;

public interface NotificationRepository extends JpaRepository <Notification, UUID>{
    
    public List<Notification> findByUser_Id(UUID id);

    // Find notifications by their status (case insensitive)
    public List<Notification> findByStatusIgnoreCase(String status);
    
    // Find notifications by their notification channel
    public List<Notification> findAllByOrderByCreatedAtDesc();
    
    // Find notifications for a specific user and status (case insensitive). Ex: "userId" and "read"
    public List<Notification> findByUser_IdAndStatusIgnoreCase(UUID userId, String status);

    // Find notifications for a specific user and notification channel. Ex: "userId" and "EMAIL"
	public List<Notification> findByUser_IdAndNotificationChannel(UUID userId, NotificationChannel channel);
	
    // Find notifications created within a specific date range
	public List<Notification> findByCreatedAtBetween(OffsetDateTime start, OffsetDateTime end);

    // Count notifications for a specific user.
	public long countByUser_Id(UUID userId);
	
    // Count notifications by their status (case insensitive)
	public long countByStatusIgnoreCase(String status);
	
    // Count notifications by their notification channel
	public long countByNotificationChannel(NotificationChannel channel);
	
    // Count notifications for a specific user and status (case insensitive). Ex: "userId" and "unread"
	public long countByUser_IdAndStatusIgnoreCase(UUID userId, String status);
	
	public void deleteByStatusIgnoreCase(String status);
	
	public void deleteByCreatedAtBefore(OffsetDateTime cutoff);
	
	public void deleteByUser_Id(UUID userId);
}