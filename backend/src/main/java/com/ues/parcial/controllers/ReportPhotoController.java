package com.ues.parcial.controllers;

import com.ues.parcial.Models.ReportPhotos;
import com.ues.parcial.dtos.reportPhoto.ReportPhotoSimpleDto;
import com.ues.parcial.mappers.ReportPhotoMapper; 
import com.ues.parcial.services.ReportPhotoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/report/{reportId}/photos")
public class ReportPhotoController {
    private final ReportPhotoService reportPhotoService;
    private final ReportPhotoMapper mapper;

    public ReportPhotoController(ReportPhotoService reportPhotoService, ReportPhotoMapper mapper) {
        this.reportPhotoService = reportPhotoService;
        this.mapper = mapper; 
    }

    @PostMapping
    public ResponseEntity<ReportPhotoSimpleDto> uploadPhoto(@PathVariable UUID reportId, @RequestParam("file") MultipartFile file,
        @RequestHeader("X-User-Id") String userId) throws IOException {
            
        ReportPhotos photo = reportPhotoService.uploadPhoto(file, reportId, userId);
        return ResponseEntity.ok(mapper.toResponseDto(photo));
    }

    @PostMapping("/multiple")
    public ResponseEntity<List<ReportPhotoSimpleDto>> uploadMultiplePhotos( 
        @PathVariable UUID reportId, @RequestParam("files") List<MultipartFile> files, @RequestHeader("X-User-Id") String userId) { 

        List<ReportPhotos> photos = reportPhotoService.uploadMultiplePhotos(files, reportId, userId);
        return ResponseEntity.ok(mapper.toResponseDto(photos));
    }

    @DeleteMapping("/{photoId}")
    public ResponseEntity<Void> deletePhoto(@PathVariable UUID reportId, @PathVariable UUID photoId) throws IOException {
        reportPhotoService.deletePhoto(photoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ReportPhotoSimpleDto>> getActivePhotos(@PathVariable UUID reportId) { 
        List<ReportPhotos> photos = reportPhotoService.getActivePhotosByReport(reportId);
        return ResponseEntity.ok(mapper.toResponseDto(photos));
    }
}