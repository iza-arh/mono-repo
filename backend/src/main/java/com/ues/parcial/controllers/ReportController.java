package com.ues.parcial.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ues.parcial.dtos.report.ReportRequestDto;
import com.ues.parcial.dtos.report.ReportResponseDto;
import com.ues.parcial.dtos.report.ReportUpdateDto;
import com.ues.parcial.dtos.report.ReportUpdateStateDto;
import com.ues.parcial.mappers.report.ReportMapper;
import com.ues.parcial.services.ReportService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {
    private final ReportService reportService;
    private final ReportMapper reportMapper;

    public ReportController(ReportService reportService, ReportMapper reportMapper) {
        this.reportService = reportService;
        this.reportMapper = reportMapper;
    }

    @PostMapping
    public ResponseEntity<?> createReport(@Valid @RequestBody ReportRequestDto dto) {
        ReportResponseDto responseDto = reportMapper.toResponseDto(reportService.createReport(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateReport(@PathVariable UUID id, @RequestBody ReportUpdateDto dto) {
        return ResponseEntity.ok(reportMapper.toResponseDto(reportService.updateReport(id, dto)));
    }

    @PatchMapping("/{id}/state")
    public ResponseEntity<?> updateReportState(@PathVariable UUID id, @Valid @RequestBody ReportUpdateStateDto dto) {
        return ResponseEntity.ok(reportMapper.toResponseDto(reportService.updateReportState(id, dto)));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivate(@PathVariable UUID id) {
        reportService.softDeleteReport(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReport(@PathVariable UUID id) {
        return ResponseEntity.ok(reportMapper.toResponseDto(reportService.getReportById(id)));
    }

    @GetMapping
    public ResponseEntity<?> getActiveReports() {
        return ResponseEntity.ok(reportMapper.toResponseDto(reportService.getAllActiveReports()));
    }

    @GetMapping("/reporter/{reporterId}")
    public ResponseEntity<?> getReportsByReporter(@PathVariable String reporterId) {
        return ResponseEntity.ok(reportMapper.toResponseDto(reportService.getReportsByReporter(reporterId)));
    }
}