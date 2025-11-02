package com.ues.parcial.services;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.ues.parcial.Models.Category;
import com.ues.parcial.Models.Report;
import com.ues.parcial.Models.User;
import com.ues.parcial.Models.Zone;
import com.ues.parcial.Models.Enums.ReportState;
import com.ues.parcial.Models.Enums.Severity;
import com.ues.parcial.dtos.category.CategorySimpleDto;
import com.ues.parcial.dtos.report.ReportRequestDto;
import com.ues.parcial.dtos.report.ReportResponseDto;
import com.ues.parcial.dtos.report.ReportUpdateDto;
import com.ues.parcial.dtos.report.ReportUpdateStateDto;
import com.ues.parcial.dtos.user.UserSimpleDto;
import com.ues.parcial.dtos.zone.GeometryDto;
import com.ues.parcial.dtos.zone.ZoneSimpleDto;
import com.ues.parcial.exceptions.ResourceNotFoundException;
import com.ues.parcial.repositories.CategoryRepository;
import com.ues.parcial.repositories.ReportRepository;
import com.ues.parcial.repositories.UserRepository;
import com.ues.parcial.repositories.ZoneRepository;
import com.ues.parcial.services.events.ReportStatusChangedEvent;
import com.ues.parcial.utils.GeometryUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.locationtech.jts.geom.Point;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportService {
    private final ReportRepository reportRepository;
    private final CategoryRepository categoryRepository;
    private final ZoneRepository zoneRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    @PersistenceContext
    private EntityManager entityManager;

    public ReportService(ReportRepository reportRepository, CategoryRepository categoryRepository, ZoneRepository zoneRepository, UserRepository userRepository, ApplicationEventPublisher eventPublisher) {
        this.reportRepository = reportRepository;
        this.categoryRepository = categoryRepository;
        this.zoneRepository = zoneRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    // Create a new report
    @Transactional
    public Report createReport(ReportRequestDto dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Zone zone = null;
        if (dto.getZoneId() != null) {
            zone = zoneRepository.findById(dto.getZoneId())
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));
        }

        User reporter = userRepository.findById(dto.getReporterId())
            .orElseThrow(() -> new ResourceNotFoundException("Reporter not found"));

        // Create Report entity
        Report r = new Report();
        r.setTitle(dto.getTitle().trim());
        r.setDescription(dto.getDescription());
        r.setCategory(category);
        r.setZone(zone);
        r.setReporter(reporter);
        r.setSeverity(dto.getSeverity() == null ? Severity.LOW : dto.getSeverity());
        r.setOccurredAt(dto.getOccurredAt());
        r.setLocationText(dto.getLocationText());
        r.setMetadata(dto.getMetadata());

        // location: admit geom.point (GeoJSON) or dto.geom.point list
        if (dto.getGeom() != null) {
            GeometryDto geom = dto.getGeom();
            if ("Point".equalsIgnoreCase(geom.getType())) {
                // use point list
                Point p = GeometryUtils.pointFromList(geom.getPoint());
                r.setLocation(p);
            } else {
                throw new IllegalArgumentException("Only Point geometry supported for report location");
            }
        } else {
            throw new IllegalArgumentException("Location (geom) is required");
        }

        Report savedReport = reportRepository.save(r);

        // Publish event for new report creation.
        eventPublisher.publishEvent(new ReportStatusChangedEvent(
            this, savedReport, null, savedReport.getState(), "New report created"));

        return savedReport;
    }

    // Update an existing report
    @Transactional
    public Report updateReport(UUID id, ReportUpdateDto dto) {
        Report r = reportRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

        ReportState oldState = r.getState();

        if (dto.getTitle() != null)
            r.setTitle(dto.getTitle().trim());

        if (dto.getDescription() != null)
            r.setDescription(dto.getDescription());

        if (dto.getCategoryId() != null) {
            Category c = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            r.setCategory(c);
        }

        if (dto.getZoneId() != null) {
            Zone z = zoneRepository.findById(dto.getZoneId())
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));
            r.setZone(z);
        }

        if (dto.getSeverity() != null)
            r.setSeverity(dto.getSeverity());

        if (dto.getState() != null)
            r.setState(dto.getState());

        if (dto.getPriority() != null)
            r.setPriority(dto.getPriority());

        if (dto.getOccurredAt() != null)
            r.setOccurredAt(dto.getOccurredAt());

        if (dto.getLocationText() != null)
            r.setLocationText(dto.getLocationText());

        if (dto.getMetadata() != null)
            r.setMetadata(dto.getMetadata());

        // Update location if provided
        if (dto.getGeom() != null) {
            GeometryDto geom = dto.getGeom();
            if ("Point".equalsIgnoreCase(geom.getType())) {
                Point p = GeometryUtils.pointFromList(geom.getPoint());
                r.setLocation(p);
            } else {
                throw new IllegalArgumentException("Only Point geometry supported for report location");
            }
        }

        Report updatedReport = reportRepository.save(r);

        // Only publish event if state changed
        if (dto.getState() != null && !dto.getState().equals(oldState)) {
            eventPublisher.publishEvent(new ReportStatusChangedEvent(
                this, updatedReport, oldState, dto.getState(), ""));
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

        return reportRepository.findByReporter(reporter);
    }
    
    // Converts Report entity to ReportResponseDto
    public ReportResponseDto toResponseDto(Report r) {
        if (r == null) return null;

        ReportResponseDto dto = new ReportResponseDto();
        dto.setId(r.getId());
        dto.setTitle(r.getTitle());
        dto.setDescription(r.getDescription());

        // CategorySimpleDto
        if (r.getCategory() != null) {
            CategorySimpleDto cat = new CategorySimpleDto();
            cat.setId(r.getCategory().getId());
            cat.setName(r.getCategory().getName());
            dto.setCategoryId(cat); 
        }

        // ZoneSimpleDto (mapear id, name; opcionalmente geom si quieres)
        if (r.getZone() != null) {
            ZoneSimpleDto zs = new ZoneSimpleDto();
            zs.setId(r.getZone().getId());
            zs.setName(r.getZone().getName());
            dto.setZoneId(zs);
        }

        // Severity / State / Priority / Dates / Text
        dto.setSeverity(r.getSeverity());
        dto.setState(r.getState());
        dto.setPriority(r.getPriority());
        dto.setOccurredAt(r.getOccurredAt());
        dto.setCreatedAt(r.getCreatedAt());
        dto.setUpdatedAt(r.getUpdatedAt());
        dto.setLocationText(r.getLocationText());

        // Reporter -> UserSimpleDto
        if (r.getReporter() != null) {
            UserSimpleDto us = new UserSimpleDto();
            us.setName(r.getReporter().getName());
            us.setLastName(r.getReporter().getLastName());
            dto.setReporter(us);
        }

        // Geometry: converts JTS Point -> GeometryDto { type: "Point", point: [lon, lat] }
        if (r.getLocation() != null && r.getLocation() instanceof Point) {
            Point p = (Point) r.getLocation();
            GeometryDto g = new GeometryDto();
            g.setType("Point");
            g.setPoint(Arrays.asList(p.getX(), p.getY())); // X = lon, Y = lat
            dto.setGeom(g);
        } else {
            dto.setGeom(null);
    }

    return dto;
}

    // Converts list of Report entities to list of ReportResponseDto
    public List<ReportResponseDto> toResponseDtoList(List<Report> reports) {
    if (reports == null) return List.of();
    return reports.stream().map(this::toResponseDto).collect(Collectors.toList());
    }
}
