package com.ues.parcial.dtos.reportPhoto;

import java.util.Map;
import java.util.UUID;

import com.ues.parcial.dtos.report.ReportSimpleDto;
import com.ues.parcial.dtos.user.UserSimpleDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportPhotoSimpleDto {
    private UUID id;
    private ReportSimpleDto reportSimpleDto;
    private UserSimpleDto uploadedBy;
    private String url;
    private String thumbnailUrl;
    private String mimeType;
    private Long sizeBytes;
    private String uploadedAt;
    private Map<String, Object> metadata;
    private boolean isActive;
}
