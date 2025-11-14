package com.ues.parcial.services;

import java.util.List;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.ues.parcial.Models.Report;
import com.ues.parcial.Models.User;
import com.ues.parcial.Models.Enums.ReportState;
import com.ues.parcial.Models.Enums.Severity;
import com.ues.parcial.dtos.report.ReportRequestDto;
import com.ues.parcial.dtos.report.ReportUpdateDto;
import com.ues.parcial.dtos.report.ReportUpdateStateDto;
import com.ues.parcial.exceptions.ResourceNotFoundException;
import com.ues.parcial.mappers.report.ReportMapper;
import com.ues.parcial.repositories.ReportRepository;
import com.ues.parcial.repositories.UserRepository;
import com.ues.parcial.services.events.ReportStatusChangedEvent;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    @PersistenceContext
    private EntityManager entityManager;
    private ReportMapper reportMapper;

    public ReportService(ReportRepository reportRepository, UserRepository userRepository, ApplicationEventPublisher eventPublisher, ReportMapper reportMapper) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
        this.reportMapper = reportMapper;
    }

    
    @Transactional
    public Report createReport(ReportRequestDto dto) {
    
        Report report = reportMapper.toEntity(dto);

        if (report.getSeverity() == null) {
            report.setSeverity(Severity.LOW);
        }

        Report savedReport = reportRepository.save(report);

        eventPublisher.publishEvent(new ReportStatusChangedEvent(
            this, savedReport, null, savedReport.getState(), "New report created"));

        return savedReport;
    }

    // Update an existing reports
    @Transactional
    public Report updateReport(UUID id, ReportUpdateDto dto) {
        Report report = reportRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

        ReportState oldState = report.getState();

        reportMapper.updateEntityFromDto(dto, report);

        Report updatedReport = reportRepository.save(report);

        // Lógica de evento
        if (dto.getState() != null && !dto.getState().equals(oldState)) {
            eventPublisher.publishEvent(new ReportStatusChangedEvent(
                this, updatedReport, oldState, dto.getState(), "Updated via partial update"));
        }

        return updatedReport;
    }

    @Transactional
    public Report updateReportState(UUID reportId, ReportUpdateStateDto dto) {
        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new ResourceNotFoundException("Report not found"));
        
        ReportState oldState = report.getState();
        
        // Only update if the new state is different
        if (!dto.getNewState().equals(oldState)) {
            report.setState(dto.getNewState());
            Report updatedReport = reportRepository.save(report);
            
            // Publish event for state change
            eventPublisher.publishEvent(new ReportStatusChangedEvent(
                this, updatedReport, oldState, dto.getNewState(), dto.getChangeReason()));
            
            return updatedReport;
        }
        
        return report;
    }

    // Soft delete a report by setting its isActive field to false
    @Transactional
    public void softDeleteReport(UUID id) {
        Report r = reportRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

        if (!r.isActive()) throw new IllegalStateException("Report already inactive");
        r.setActive(false);
        reportRepository.save(r);
    }

    // Find report by ID
    @Transactional(readOnly = true)
    public Report getReportById(UUID id) {
        return reportRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Report not found with ID: " + id));
    }

    // Get all active reports
    @Transactional(readOnly = true)
    public List<Report> getAllActiveReports() {
        return reportRepository.findByIsActiveTrue();
    }

    @Transactional(readOnly = true)
    public List<Report> getReportsByReporter(String reporterId) {
        User reporter = userRepository.findById(reporterId)
            .orElseThrow(() -> new ResourceNotFoundException("Reporter not found"));

        return reportRepository.findByReporterAndIsActiveTrue(reporter);
    }
}
