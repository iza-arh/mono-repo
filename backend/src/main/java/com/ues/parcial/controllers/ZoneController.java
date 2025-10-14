package com.ues.parcial.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ues.parcial.dtos.zone.ZoneRequestDto;
import com.ues.parcial.dtos.zone.ZoneResponseDto;
import com.ues.parcial.dtos.zone.ZoneUpdateDto;
import com.ues.parcial.services.ZoneService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/zones")
public class ZoneController {
    
    private final ZoneService zoneService;

    public ZoneController(ZoneService zoneService){
        this.zoneService = zoneService;
    }

    @PostMapping
    public ResponseEntity<?> createZone(@Valid @RequestBody ZoneRequestDto dto) {
        ZoneResponseDto response = zoneService.toResponseDto(zoneService.createZone(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateZone(@PathVariable UUID id, @Valid @RequestBody ZoneUpdateDto dto) {
        return ResponseEntity.ok(zoneService.toResponseDto(zoneService.updateZone(id, dto)));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateZone(@PathVariable UUID id) {
        zoneService.updateZoneStatus(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getZoneById(@PathVariable UUID id) {
        return ResponseEntity.ok(zoneService.toResponseDto(zoneService.getZoneById(id)));
    }

    @GetMapping
    public ResponseEntity<?> getAllZones() {
        return ResponseEntity.ok(zoneService.toResponseDto(zoneService.getAllZones()));
    }
}