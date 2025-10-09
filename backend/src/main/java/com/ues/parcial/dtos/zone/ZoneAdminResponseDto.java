package com.ues.parcial.dtos.zone;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

import org.locationtech.jts.geom.Polygon;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ZoneAdminResponseDto {
    
    private UUID id;
    private String name;
    private Polygon geom;
    private Map<String, Object> metadata;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

}
