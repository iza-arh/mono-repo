package com.ues.parcial.dtos.zone;

import java.util.Map;
import java.util.UUID;

import org.locationtech.jts.geom.Polygon;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ZoneResponseDto {

    private UUID id;
    private String name;
    private Polygon geom;
    private Map<String, Object> metadata;
    
}
