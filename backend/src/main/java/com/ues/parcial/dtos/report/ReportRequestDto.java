package com.ues.parcial.dtos.report;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;
import com.ues.parcial.Models.Enums.Severity;
import com.ues.parcial.dtos.zone.GeometryDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReportRequestDto {
    @NotBlank(message = "Title is required")
    private String title;
    private String description;

    //@NotNull(message = "Category is required")
    private Long categoryId;

    @NotNull(message = "Zone ID is required")
    private UUID zoneId; 

    @NotNull(message = "Reporter ID is required")
    private String reporterId;

    private Severity severity; 
    private OffsetDateTime occurredAt; 

    @NotNull(message = "Geometry is required")
    private GeometryDto geom;

    private String locationText;

    private Map<String,Object> metadata;
}
