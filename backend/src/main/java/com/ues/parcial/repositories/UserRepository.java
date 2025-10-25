package com.ues.parcial.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ues.parcial.Models.User;
import com.ues.parcial.Models.Enums.UserRole;

public interface UserRepository extends JpaRepository <User, UUID>{
    
    public Optional<User> findByEmailIgnoreCase(String email);

    public List<User> findByRoleIgnoreCase(UserRole role);

    public Optional<User> findByPhone(String phone);

    public List<User> findByIsActive(Boolean isActive);

    //  Finds users by role and active status combination (ex: find all active citizens).
    List<User> findByRoleAndIsActive(UserRole role, Boolean isActive);

    Boolean existsByEmailIgnoreCase(String email);

    Boolean existsByPhone(String phone);

    // Finds all users ordered by creation date (newest first).
    List<User> findAllByOrderByCreatedAtDesc();

    // Finds active users ordered by creation date (newest first).
    List<User> findByIsActiveTrueOrderByCreatedAtDesc();
}