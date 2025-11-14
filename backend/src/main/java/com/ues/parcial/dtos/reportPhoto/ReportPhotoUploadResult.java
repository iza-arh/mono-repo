package com.ues.parcial.dtos.reportPhoto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ReportPhotoUploadResult {
    
    private final String url;
    private final String thumbnailUrl;
    private final String publicId;
    private final String mimeType;
    private final Long sizeBytes;
    private final Map<String, Object> metadata;
}
