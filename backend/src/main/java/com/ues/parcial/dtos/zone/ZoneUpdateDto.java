package com.ues.parcial.dtos.zone;

import java.util.Map;

import org.locationtech.jts.geom.Polygon;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ZoneUpdateDto {

    @Size(max = 100, message = "El nombre de la zona no puede tener más de 100 caracteres.")
    private String name;

    private Polygon geom;

    private Map<String, Object> metadata;
}