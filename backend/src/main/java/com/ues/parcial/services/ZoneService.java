package com.ues.parcial.services;


import java.util.List;
import java.util.UUID;

import org.apache.commons.text.WordUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ues.parcial.Models.Zone;
import com.ues.parcial.dtos.zone.GeometryDto;
import com.ues.parcial.dtos.zone.ZoneRequestDto;
import com.ues.parcial.dtos.zone.ZoneResponseDto;
import com.ues.parcial.dtos.zone.ZoneUpdateDto;
import com.ues.parcial.exceptions.InvalidGeometryException;
import com.ues.parcial.exceptions.ResourceAlreadyExistsException;
import com.ues.parcial.exceptions.ResourceNotFoundException;
import com.ues.parcial.repositories.ZoneRepository;
import com.ues.parcial.utils.ListUtils;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.geom.Coordinate;

@Service
public class ZoneService {
    
    private final ZoneRepository zoneRepository;

    public ZoneService(ZoneRepository zoneRepository){
        this.zoneRepository = zoneRepository;
    }

    @Transactional
    public Zone createZone(ZoneRequestDto dto) {
        // Capitalize the first letter of each word in the name
        String formattedName = WordUtils.capitalizeFully(dto.getName().trim());

        zoneRepository.findByNameIgnoreCase(formattedName)
            .ifPresent(p -> { 
                throw new ResourceAlreadyExistsException("There is already a zone with that name: " + dto.getName()); 
            });

        Zone zone = new Zone();
        zone.setName(formattedName);
        
        // Convert GeometryDto to Polygon - any exception will be handled by GlobalExceptionHandler
        Polygon polygon = convertGeometryDtoToPolygon(dto.getGeom());
        zone.setGeom(polygon);
        
        zone.setMetadata(dto.getMetadata());
        return zoneRepository.save(zone);
    }

    // Converts a GeometryDto to a JTS Polygon with SRID 4326 (WGS84).
    private Polygon convertGeometryDtoToPolygon(GeometryDto geometryDto) {
        if (!"Polygon".equals(geometryDto.getType())) {
            throw new InvalidGeometryException("Only 'Polygon' type is supported");
        }
        
        List<List<List<Double>>> coordinates = geometryDto.getCoordinates();
        if (coordinates == null || coordinates.isEmpty()) {
            throw new InvalidGeometryException("Coordinates cannot be empty");
        }
        
        // Create GeometryFactory with SRID 4326
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);
        
        // Convert coordinates to LinearRing
        List<List<Double>> ring = coordinates.get(0); // First ring (exterior)
        Coordinate[] coords = new Coordinate[ring.size()];
        
        for (int i = 0; i < ring.size(); i++) {
            List<Double> coord = ring.get(i);
            if (coord.size() < 2) {
                throw new InvalidGeometryException("Each coordinate must have at least 2 values");
            }
            coords[i] = new Coordinate(coord.get(0), coord.get(1));
        }
        
        // Create LinearRing and Polygon
        LinearRing shell = geometryFactory.createLinearRing(coords);
        return geometryFactory.createPolygon(shell);
    }

    @Transactional
    public Zone updateZone(UUID id, ZoneUpdateDto dto) {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found with ID: " + id));

        // Update only the fields that are not null in the DTO

        if (dto.getName() != null) {
            String formattedName = WordUtils.capitalizeFully(dto.getName().trim());

            // If the name is changing, check for uniqueness among active zones
            String currentName = zone.getName() == null ? "" : zone.getName().trim();

            if (!formattedName.equalsIgnoreCase(currentName)) {
                // Check if another active zone (not the current one) already has this name
                boolean exists = zoneRepository.existsByNameIgnoreCaseAndIsActiveTrueAndIdNot(formattedName, id);

                if (exists) {
                    throw new ResourceAlreadyExistsException("There is already an active zone with that name: " + formattedName);
                }
            }
            zone.setName(formattedName);
        }

        if (dto.getGeom() != null) {
            Polygon polygon = convertGeometryDtoToPolygon(dto.getGeom());
            zone.setGeom(polygon);
        }

        if (dto.getMetadata() != null) {
            zone.setMetadata(dto.getMetadata());
        }

        if (dto.getIsActive() != null) {
            zone.setActive(dto.getIsActive());
        }

        return zoneRepository.save(zone);
    }

    @Transactional(readOnly = true)
    public Zone getZoneById(UUID id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found with ID: " + id));
    }
    
    @Transactional(readOnly = true)
    public List<Zone> getAllZones() {
        return ListUtils.emptyIfNull(zoneRepository.findByIsActiveTrue());
    }

    // Convert a Zone entity to a ZoneResponseDto
    public ZoneResponseDto toResponseDto(Zone zone){
        ZoneResponseDto dto = new ZoneResponseDto();
        dto.setId(zone.getId());
        dto.setName(zone.getName());
        dto.setGeom(zone.getGeom());
        dto.setMetadata(zone.getMetadata());
        return dto;
    }

    // Convert a list of Zone entities to a list of ZoneResponseDto
    public List<ZoneResponseDto> toResponseDto(List<Zone> zones) {
        return zones.stream().map(this::toResponseDto).toList();
    }
}