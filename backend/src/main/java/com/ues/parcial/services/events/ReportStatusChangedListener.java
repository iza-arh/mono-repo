package com.ues.parcial.services.events;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.ues.parcial.Models.Report;
import com.ues.parcial.Models.Enums.NotificationChannel;
import com.ues.parcial.Models.Enums.ReportState;
import com.ues.parcial.dtos.notification.CreateNotificationDto;
import com.ues.parcial.services.EmailService;
import com.ues.parcial.services.NotificationService;

@Component
public class ReportStatusChangedListener {
    
    private static final Logger logger = LoggerFactory.getLogger(ReportStatusChangedListener.class);
    private final EmailService emailService;
    private final NotificationService notificationService;
    
    public ReportStatusChangedListener(EmailService emailService, NotificationService notificationService) {
        this.emailService = emailService;
        this.notificationService = notificationService;
    }
    
    @EventListener
    @Async
    public void handleReportStatusChanged(ReportStatusChangedEvent event) {
        Report report = event.getReport();
        ReportState newState = event.getNewState();
        ReportState oldState = event.getOldState();
        
        logger.info("Processing status change for report {}: {} -> {}", 
                    report.getId(), oldState, newState);
        
        // Only notify if the report is active and there is a real state change
        if (shouldNotifyUser(report, oldState, newState)) {
            sendStatusChangeEmail(report, oldState, newState, event.getChangeReason());
            saveNotificationRecord(report, oldState, newState, event.getChangeReason()); // Save notification in db
        }
    }
    
    private boolean shouldNotifyUser(Report report, ReportState oldState, ReportState newState) {
        // Notify only if: 1. Report is active, 2. State has actually change and 3. There is a reporter associated with the report
        return report.isActive() 
            && !newState.equals(oldState)
            && report.getReporter() != null;
    }
    
    private void sendStatusChangeEmail(Report report, ReportState oldState, ReportState newState, String changeReason) {
        try {
            emailService.sendReportStatusEmail(report, oldState, newState, changeReason);
            
            logger.info("Status change email sent for report {} to user {}", 
                        report.getId(), report.getReporter().getId());
            
        } catch (Exception e) {
            logger.error("Error sending status change email for report {}: {}", 
                        report.getId(), e.getMessage(), e);
        }
    }
    
    // Save notification record without sending email
    private void saveNotificationRecord(Report report, ReportState oldState, ReportState newState, String changeReason) {
        try {
            CreateNotificationDto notificationDto = new CreateNotificationDto();
            notificationDto.setUserId(report.getReporter().getId());
            notificationDto.setChannel(NotificationChannel.EMAIL);
            notificationDto.setSendNotification(false); // No email sending, just save record because email already sent
            
            Map<String, Object> payload = buildNotificationPayload(report, oldState, newState, changeReason);
            notificationDto.setPayload(payload);
            
            notificationService.createNotification(notificationDto);
            
            logger.debug("Notification record saved for report {} status change", report.getId());
            
        } catch (Exception e) {
            logger.warn("Failed to save notification record for report {}: {}", 
                        report.getId(), e.getMessage());
        }
    }
    
    // Build payload for notification record.
    private Map<String, Object> buildNotificationPayload(Report report, ReportState oldState, ReportState newState, String changeReason) {
        Map<String, Object> payload = new HashMap<>();

        String oldStateStr = (oldState != null) ? oldState.toString() : "New Report";
        
        payload.put("subject", "Update on Your Report Status");
        payload.put("title", "Report Status Updated");
        payload.put("message", buildStatusMessage(report, oldState, newState, changeReason));
        payload.put("reportId", report.getId().toString());
        payload.put("reportTitle", report.getTitle());
        payload.put("oldStatus", oldStateStr);
        payload.put("newStatus", newState.toString());
        payload.put("userName", report.getReporter().getName());
        payload.put("changeDate", java.time.OffsetDateTime.now().toString());
        
        if (changeReason != null && !changeReason.trim().isEmpty()) {
            payload.put("changeReason", changeReason);
        }
        
        payload.put("viewReportLink", buildReportViewLink(report.getId()));
        
        return payload;
    }
    
    // Build the status change message for notification payload
    private String buildStatusMessage(Report report, ReportState oldState, ReportState newState, String changeReason) {

        String oldStateStr = (oldState != null) ? oldState.toString() : "New Report";
        
        String baseMessage = String.format(
            "Hello %s,\n\nThe status of your report \\\"%s\\\" has changed from %s to %s.",
            report.getReporter().getName(),
            report.getTitle(),
            oldStateStr,
            newState.toString()
        );
        
        String statusMessage = getStatusSpecificMessage(newState);
        String reasonMessage = changeReason != null ? 
            String.format("\n\nReason for the change: %s", changeReason) : "";
        
        return baseMessage + statusMessage + reasonMessage + 
                "\n\nYou can view the details of your report at the following link:" + 
                buildReportViewLink(report.getId());
    }
    
    private String getStatusSpecificMessage(ReportState newState) {
        switch (newState) {
            case VALIDATED:
                return "\n\nYour report has been validated and is now being processed by the appropriate authorities.";
            case REJECTED:
                return "\n\nUnfortunately, your report does not meet the necessary criteria to be processed.";
            case ASSIGNED:
                return "\n\nYour report has been assigned to a specialized team for handling.";
            case IN_PROGRESS:
                return "\n\nYour report is currently being actively handled by the corresponding team.";
            case RESOLVED:
                return "\n\nGreat news! The reported issue has been successfully resolved.";
            case NO_PROCEDE:
                return "\n\nYour report cannot proceed according to the established guidelines.";
            default:
                return "\n\nThe report has progressed in its handling process.";
        }
    }
    
    // Build link to view the report
    private String buildReportViewLink(java.util.UUID reportId) {
        return String.format("https://urbanofix.com/reports/%s", reportId.toString());
    }
}