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
}