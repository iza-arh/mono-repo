package com.ues.parcial.services.events;

import org.springframework.context.ApplicationEvent;

import com.ues.parcial.Models.Report;
import com.ues.parcial.Models.Enums.ReportState;

import lombok.Getter;

@Getter
public class ReportStatusChangedEvent extends ApplicationEvent {
    private final Report report;
    private final ReportState oldState;
    private final ReportState newState;
    private final String changeReason; // Optionally, reason for the change

    public ReportStatusChangedEvent(Object source, Report report, ReportState oldState, ReportState newState, String changeReason) {
        super(source);
        this.report = report;
        this.oldState = oldState;
        this.newState = newState;
        this.changeReason = changeReason;
    }
}