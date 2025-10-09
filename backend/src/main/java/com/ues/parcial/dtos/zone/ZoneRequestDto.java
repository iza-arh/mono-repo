package com.ues.parcial.dtos.zone;

import java.util.Map;

import org.locationtech.jts.geom.Polygon;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ZoneRequestDto {
    
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be at most 100 characters")
    private String name;

    @NotNull(message = "Geometry is required")
    private Polygon geom;

    // Optional metadata field.
    private Map<String, Object> metadata; 

}
