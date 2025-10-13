package com.ues.parcial.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ues.parcial.Models.Zone;

public interface ZoneRepository extends JpaRepository<Zone, UUID> {

    // It finds a zone by its name, ignoring case and allowing partial matches.
    public List<Zone> findByNameIgnoreCaseContaining(String name);

    // It finds a zone by its name ingnoring case.
    public Optional<Zone> findByNameIgnoreCase(String name);

    // It retrieves all zones that are marked as active.
    List<Zone> findByIsActiveTrue();

    // It checks if a zone with the given name exists, ignoring case and considering only active zones.
    boolean existsByNameIgnoreCaseAndIsActiveTrue(String name);

    // It checks if a zone with the given name exists, ignoring case and considering only active zones, excluding a specific zone by its ID.
    boolean existsByNameIgnoreCaseAndIsActiveTrueAndIdNot(String name, UUID id);
}