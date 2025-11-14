package com.ues.parcial.services;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ues.parcial.Models.Report;
import com.ues.parcial.Models.ReportPhotos;
import com.ues.parcial.Models.User;
import com.ues.parcial.dtos.reportPhoto.ReportPhotoUploadResult;
import com.ues.parcial.exceptions.ResourceNotFoundException;
import com.ues.parcial.exceptions.fileExceptions.CloudStorageException;
import com.ues.parcial.exceptions.fileExceptions.InvalidFileException;
import com.ues.parcial.repositories.ReportPhotoRepository;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class ReportPhotoService {
    
    private final ReportPhotoRepository reportPhotoRepository;
    private final CloudinaryService cloudinaryService;
    private final ReportService reportService;
    private final UserService userService;

    public ReportPhotoService(ReportPhotoRepository reportPhotoRepository, CloudinaryService cloudinaryService, ReportService reportService, UserService userService) {
        this.reportPhotoRepository = reportPhotoRepository;
        this.cloudinaryService = cloudinaryService;
        this.reportService = reportService;
        this.userService = userService;
    }

    @Transactional
    public ReportPhotos uploadPhoto(MultipartFile multipartFile, UUID reportId, String userId) throws IOException {
        
        Report report = reportService.getReportById(reportId);
        User user = userService.getUserById(userId);
        
        ReportPhotoUploadResult uploadResult = cloudinaryService.uploadPhoto(multipartFile, reportId);
        
        ReportPhotos reportPhoto = buildReportPhoto(uploadResult, report, user);
        
        ReportPhotos savedPhoto = reportPhotoRepository.save(reportPhoto);
        
        return savedPhoto;
    }

    @Transactional
    public List<ReportPhotos> uploadMultiplePhotos(List<MultipartFile> files, UUID reportId, String userId) {
        
        return files.stream()
                .map(file -> {
                    try {
                        return uploadPhoto(file, reportId, userId);
                    } catch (IOException e) {
                        throw new InvalidFileException("Failed to upload file: " + file.getOriginalFilename(), e);
                    }
                })
                .toList();
    }

    @Transactional
    public void deletePhoto(UUID photoId) throws IOException {
        
        ReportPhotos photo = reportPhotoRepository.findById(photoId)
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found with ID: " + photoId));
        
        photo.setActive(false);
        reportPhotoRepository.save(photo);
        
        try {
            //delete from cloudinary
            String publicId = (String) photo.getMetadata().get("public_id");
            if (publicId != null) {
                cloudinaryService.deletePhoto(publicId);
            }
            
            // if deletion from cloud is successful, delete from database
            reportPhotoRepository.delete(photo);
            
        } catch (IOException e) {
            throw new CloudStorageException("Failed to delete photo from cloud storage", e);
        }
    }

    @Transactional
    public void softDeletePhoto(UUID photoId) {
        
        ReportPhotos photo = reportPhotoRepository.findById(photoId)
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found with ID: " + photoId));
        
        photo.setActive(false);
        reportPhotoRepository.save(photo);
    }

    @Transactional(readOnly = true)
    public List<ReportPhotos> getActivePhotosByReport(UUID reportId) {
        return reportPhotoRepository.findByReportIdAndIsActiveTrue(reportId);
    }

    private ReportPhotos buildReportPhoto(ReportPhotoUploadResult uploadResult, Report report, User user) {
        ReportPhotos reportPhoto = new ReportPhotos();
        reportPhoto.setUrl(uploadResult.getUrl());
        reportPhoto.setThumbnailUrl(uploadResult.getThumbnailUrl());
        reportPhoto.setMimeType(uploadResult.getMimeType());
        reportPhoto.setSizeBytes(uploadResult.getSizeBytes());
        reportPhoto.setMetadata(uploadResult.getMetadata());
        reportPhoto.setReport(report);
        reportPhoto.setUploadedBy(user);
        reportPhoto.setActive(true);
        
        return reportPhoto;
    }
}