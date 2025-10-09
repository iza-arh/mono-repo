package com.ues.parcial.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ues.parcial.Models.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
    
    // Find category by code
    public Optional<Category> findByCode(String code);

    // Find category by name (case insensitive)
    public Optional<Category> findByNameIgnoreCase(String name);

    // Find all active categories
    public List<Category> findByIsActiveTrue();

}