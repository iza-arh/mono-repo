package com.ues.parcial.dtos.report;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

import com.ues.parcial.Models.Enums.ReportState;
import com.ues.parcial.Models.Enums.Severity;
import com.ues.parcial.dtos.zone.GeometryDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReportUpdateDto {
    private String title;
    private String description;

    private Long categoryId;
    private UUID zoneId;
    private String reporterId; 

    private Severity severity;
    private ReportState state;
    private Integer priority;

    private OffsetDateTime occurredAt;

    private GeometryDto geom;
    private String locationText;
    
    private Map<String,Object> metadata;
    
}
