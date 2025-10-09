package com.ues.parcial.services;


import java.util.List;
import java.util.UUID;

import org.apache.commons.text.WordUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ues.parcial.Models.Zone;
import com.ues.parcial.dtos.zone.ZoneRequestDto;
import com.ues.parcial.dtos.zone.ZoneResponseDto;
import com.ues.parcial.dtos.zone.ZoneUpdateDto;
import com.ues.parcial.exceptions.ResourceAlreadyExistsException;
import com.ues.parcial.exceptions.ResourceNotFoundException;
import com.ues.parcial.repositories.ZoneRepository;
import com.ues.parcial.utils.ListUtils;

@Service
public class ZoneService {
    
    ZoneRepository zoneRepository;

    public ZoneService(ZoneRepository zoneRepository){
        this.zoneRepository = zoneRepository;
    }

    @Transactional
    public Zone createZone(ZoneRequestDto dto) {

        // Capitalize the first letter of each word in the name
        String formattedName = WordUtils.capitalizeFully(dto.getName().trim());

        zoneRepository.findByNameIgnoreCase(formattedName)
            .ifPresent(p -> { throw new ResourceAlreadyExistsException("There is already a zone with that name: " + dto.getName()); });;

        Zone zone = new Zone();
        zone.setName(formattedName);
        zone.setGeom(dto.getGeom());
        zone.setMetadata(dto.getMetadata());
        // createdAt y updatedAt are set automatically by the @PrePersist method in the Zone model

        return zoneRepository.save(zone);
    }

    @Transactional
    public Zone updateZone(UUID id, ZoneUpdateDto dto) {

        Zone zone = zoneRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Zone not found with ID: " + id));

        // Update only the fields that are not null in the DTO

        if (dto.getName() != null) {
            String formattedName = WordUtils.capitalizeFully(dto.getName().trim());
            zone.setName(formattedName);
        }

        if (dto.getGeom() != null) {
            zone.setGeom(dto.getGeom());
        }

        if (dto.getMetadata() != null) {
            zone.setMetadata(dto.getMetadata());
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
        return ListUtils.emptyIfNull(zoneRepository.findAll());
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