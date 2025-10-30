package com.ues.parcial.dtos.report;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.ues.parcial.Models.Enums.ReportState;
import com.ues.parcial.Models.Enums.Severity;
import com.ues.parcial.dtos.category.CategorySimpleDto;
import com.ues.parcial.dtos.user.UserSimpleDto;
import com.ues.parcial.dtos.zone.GeometryDto;
import com.ues.parcial.dtos.zone.ZoneSimpleDto;

import lombok.Data;

@Data
public class ReportResponseDto {
    private UUID id;
    private String title;
    private String description;
    private CategorySimpleDto categoryId;
    private ZoneSimpleDto zoneId;
    
    private UserSimpleDto reporter; 
    
    private ReportState state;
    private Severity severity;
    private Integer priority;

    private OffsetDateTime occurredAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    
    private GeometryDto geom;
    private String locationText;

}
