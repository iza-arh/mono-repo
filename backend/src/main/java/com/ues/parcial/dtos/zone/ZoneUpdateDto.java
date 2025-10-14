package com.ues.parcial.dtos.zone;

import java.util.Map;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ZoneUpdateDto {

    @Size(max = 100, message = "Name must be at most 100 characters.")
    private String name;

    private GeometryDto geom; // This will be converted to Polygon in the service layer.

    private Map<String, Object> metadata;
}