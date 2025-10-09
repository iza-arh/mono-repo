package com.ues.parcial.services;


import java.util.List;

import org.apache.commons.text.WordUtils;
import org.springframework.stereotype.Service;

import com.ues.parcial.Models.Category;
import com.ues.parcial.dtos.category.CategoryRequestDto;
import com.ues.parcial.dtos.category.CategoryResponseDto;
import com.ues.parcial.dtos.category.CategoryUpdateDto;
import com.ues.parcial.exceptions.ResourceAlreadyExistsException;
import com.ues.parcial.exceptions.ResourceNotFoundException;
import com.ues.parcial.repositories.CategoryRepository;
import com.ues.parcial.utils.ListUtils;

import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {
    
    CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // ------------------------------------------------------------------
    // Creation operations
    // ------------------------------------------------------------------

    // Create a new category
    @Transactional
    public Category createCategory(CategoryRequestDto dto) {

        // Capitalize the first letter of each word in the name
        String formattedName = WordUtils.capitalizeFully(dto.getName().trim());
    
        categoryRepository.findByNameIgnoreCase(formattedName)
        .ifPresent(c -> { throw new ResourceAlreadyExistsException("There is already a category with that name: " + formattedName); });

        Category category = new Category();
        category.setName(formattedName);
        category.setCode(dto.getCode().trim());
        // createdAt y updatedAt are set automatically by the @PrePersist method in the Zone model
        
        return categoryRepository.save(category);
    }

    // ------------------------------------------------------------------
    // Update operations
    // ------------------------------------------------------------------

    // Update an existing category
    @Transactional
    public Category updateCategory(Long id, CategoryUpdateDto dto) {
        
        Category category = categoryRepository.findById(id)
                                            .orElseThrow(() -> new ResourceNotFoundException("Zone not found with ID: " + id));

        // Update only the fields that are not null in the DTO
        if (dto.getName() != null) {
            String formattedName = WordUtils.capitalizeFully(dto.getName().trim());
        
            categoryRepository.findByNameIgnoreCase(formattedName)
            .ifPresent(c -> { throw new ResourceAlreadyExistsException("There is already a category with that name: " + formattedName); });

            category.setName(formattedName);
        }

        if (dto.getCode() != null) {
            category.setCode(dto.getCode().trim());
        }

        if (dto.getIsActive() != null) {
            category.setActive(dto.getIsActive());
        }

        return categoryRepository.save(category);
    }

    // ------------------------------------------------------------------
    // Query methods
    // ------------------------------------------------------------------

    // Get a category by its ID
    @Transactional(readOnly = true)
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
    }

    // Get all categories
    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return ListUtils.emptyIfNull(categoryRepository.findByIsActiveTrue());  
    }

    // Convert Category entity to CategoryResponseDto
    public CategoryResponseDto toResponseDto (Category category){
        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setId(category.getId());
        dto.setCode(category.getCode());
        dto.setName(category.getName());
        return dto;
    }

    // Convert a list of Category entities to a list of CategoryResponseDto
    public List<CategoryResponseDto> toResponseDtoList(List<Category> categories){
        return categories.stream().map(this::toResponseDto).toList();
    }
}