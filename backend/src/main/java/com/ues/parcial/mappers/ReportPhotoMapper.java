package com.ues.parcial.mappers;

import com.ues.parcial.Models.ReportPhotos;
import com.ues.parcial.dtos.category.CategorySimpleDto;
import com.ues.parcial.dtos.report.ReportSimpleDto;
import com.ues.parcial.dtos.reportPhoto.ReportPhotoSimpleDto;
import com.ues.parcial.dtos.user.UserSimpleDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component 
public class ReportPhotoMapper {

    // Convert a single ReportPhotos to ReportPhotoSimpleDto
    public ReportPhotoSimpleDto toResponseDto(ReportPhotos reportPhoto) {

        CategorySimpleDto categorySimpleDto = new CategorySimpleDto();
        categorySimpleDto.setId(reportPhoto.getReport().getCategory().getId());
        categorySimpleDto.setName(reportPhoto.getReport().getCategory().getName());

        ReportSimpleDto reportSimpleDto = new ReportSimpleDto();
        reportSimpleDto.setId(reportPhoto.getReport().getId());
        reportSimpleDto.setTitle(reportPhoto.getReport().getTitle());
        reportSimpleDto.setCategorySimpleDto(categorySimpleDto);

        UserSimpleDto userSimpleDto = new UserSimpleDto();
        userSimpleDto.setName(reportPhoto.getUploadedBy().getName());
        userSimpleDto.setLastName(reportPhoto.getUploadedBy().getLastName());
        userSimpleDto.setEmail(reportPhoto.getUploadedBy().getEmail());
        
        ReportPhotoSimpleDto reportPhotoSimpleDto = new ReportPhotoSimpleDto();
        reportPhotoSimpleDto.setId(reportPhoto.getId());
        reportPhotoSimpleDto.setUploadedBy(userSimpleDto);
        reportPhotoSimpleDto.setReportSimpleDto(reportSimpleDto);
        reportPhotoSimpleDto.setUrl(reportPhoto.getUrl());
        reportPhotoSimpleDto.setThumbnailUrl(reportPhoto.getThumbnailUrl());
        reportPhotoSimpleDto.setMimeType(reportPhoto.getMimeType());
        reportPhotoSimpleDto.setSizeBytes(reportPhoto.getSizeBytes());
        reportPhotoSimpleDto.setUploadedAt(reportPhoto.getUploadedAt().toString());
        reportPhotoSimpleDto.setMetadata(reportPhoto.getMetadata());
        reportPhotoSimpleDto.setActive(reportPhoto.isActive());

        return reportPhotoSimpleDto;
    }

    // Convert a list of ReportPhotos to a list of ReportPhotoSimpleDto
    public List<ReportPhotoSimpleDto> toResponseDto (List<ReportPhotos> reportPhotos) {
        return reportPhotos.stream()
                .map(this::toResponseDto)
                .toList();
    }
}