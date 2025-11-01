package com.ues.parcial.services;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.fasterxml.jackson.databind.JsonNode;
import com.ues.parcial.Models.Notification;
import com.ues.parcial.Models.Report;
import com.ues.parcial.Models.Enums.NotificationChannel;
import com.ues.parcial.Models.Enums.ReportState;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    /**
     * Sends an email based on the notification payload.
     * Variables are extracted from the Notification payload (JSON).
     */
    public void sendNotificationEmail(Notification notification) throws MessagingException {
        if (notification == null || notification.getUser() == null || notification.getNotificationChannel() != NotificationChannel.EMAIL) {
            return; // skip if notification is invalid or not email
        }

        JsonNode payload = notification.getPayload();
        Map<String, Object> variables = new HashMap<>();

        // Extract variables from payload
        if (payload != null && payload.isObject()) {
            Map<String, String> expectedKeys = Map.of(
                "subject", "title",
                "message", "message",
                "name", "name",
                "lastname", "lastname",
                "link", "link"
            );

            // Map payload keys to template variable keys
            expectedKeys.forEach((jsonKey, variableKey) -> {
                JsonNode value = payload.get(jsonKey);
                if (value != null && !value.isNull()) {
                    variables.put(variableKey, value.asText());
                }
            });
        }

        // Add user data (in case payload didn't include it)
        if (!variables.containsKey("name")) {
            variables.put("name", notification.getUser().getName());
        }
        if (!variables.containsKey("lastname")) {
            variables.put("lastname", notification.getUser().getLastName());
        }

        // Prepare Thymeleaf context
        Context context = new Context();
        context.setVariables(variables);

        // Process template
        String htmlContent = templateEngine.process("notification-template", context);

        // Build the email
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(notification.getUser().getEmail());
        helper.setSubject((String) variables.getOrDefault("title", "Notification System"));
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    // Send report status email with specific template based on state
    public void sendReportStatusEmail(Report report, ReportState oldState, ReportState newState, String changeReason) 
            throws MessagingException {
        
        if (report == null || report.getReporter() == null) {
            logger.warn("Cannot send email: report or reporter is null");
            return;
        }
        
        try {
            // Prepare template variables
            Map<String, Object> variables = prepareReportTemplateVariables(report, oldState, newState, changeReason);
            
            // This will select the appropriate template based on the new state
            String templateName = getCompleteTemplateForState(newState);
            String emailSubject = getEmailSubjectForState(newState, report.getTitle());
            
            logger.info("Using template: {}", templateName);
            
            Context context = new Context();
            context.setVariables(variables);
            String fullEmailContent = templateEngine.process(templateName, context);
            
            sendReportEmail(report.getReporter().getEmail(), emailSubject, fullEmailContent); // Send email
            
            logger.info("Email sent successfully for report {} status change: {} -> {}", 
                    report.getId(), oldState, newState);
            
        } catch (Exception e) {
            logger.error("Failed to send report status email for report {}: {}", 
                        report.getId(), e.getMessage(), e);
            throw e;
        }
    }
    
    // Prepare variables for the report status email template
    private Map<String, Object> prepareReportTemplateVariables(Report report, ReportState oldState, ReportState newState, String changeReason) {
        Map<String, Object> variables = new HashMap<>();

        String oldStateStr = (oldState != null) ? oldState.toString() : "New Report";
        
        variables.put("userName", report.getReporter().getName());
        variables.put("reportTitle", report.getTitle());
        variables.put("reportId", report.getId().toString());
        variables.put("oldStatus", oldStateStr);
        variables.put("newStatus", newState.toString());
        variables.put("changeDate", formatDate(java.time.OffsetDateTime.now()));
        variables.put("changeReason", changeReason);
        variables.put("viewReportLink", buildReportViewLink(report.getId()));
        variables.put("emailTitle", getEmailTitleForState(newState));
        
        return variables;
    }
    
    // Determine the complete template name based on report state
    private String getCompleteTemplateForState(ReportState state) {
        return "emails/reports/status-changed/" + state.name().toLowerCase();
    }
    
    // Title for the email based on report state
    private String getEmailTitleForState(ReportState state) {
        switch (state) {
            case VALIDATED: return "Report Validated- UrbanoFix";
            case REJECTED: return "Report Rejected - UrbanoFix";
            case ASSIGNED: return "Report Assigned - UrbanoFix";
            case IN_PROGRESS: return "Report in Progress - UrbanoFix";
            case RESOLVED: return "Report Resolved! - UrbanoFix";
            case NO_PROCEDE: return "Report No Procede - UrbanoFix";
            default: return "Report update - UrbanoFix";
        }
    }
    
    // Subject line for the email based on report state
    private String getEmailSubjectForState(ReportState state, String reportTitle) {
        switch (state) {
            case VALIDATED: return "✅ Your report has been validated: " + reportTitle;
            case REJECTED: return "❌ Report rejected: " + reportTitle;
            case ASSIGNED: return "👥 Report assigned to team: " + reportTitle;
            case IN_PROGRESS: return "🛠️ Report in progress: " + reportTitle;
            case RESOLVED: return "🎉 Report resolved!: " + reportTitle;
            case NO_PROCEDE: return "Reporte no procede: " + reportTitle;
            default: return "Report update: " + reportTitle;
        }
    }
    
    // Format date to a readable string
    private String formatDate(java.time.OffsetDateTime date) {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy 'a las' HH:mm");
        return date.format(formatter);
    }
    
    // Build link to view the report
    private String buildReportViewLink(java.util.UUID reportId) {
        return "https://urbanofix.com/reports/" + reportId.toString();
    }
    
    // Send the email with given content
    private void sendReportEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        
        mailSender.send(message);
    }

    // Plain text option in case we ever need to use only plain text without a template
    public void sendPlainEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}