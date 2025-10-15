package com.ues.parcial.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ues.parcial.dtos.category.CategoryRequestDto;
import com.ues.parcial.dtos.category.CategoryResponseDto;
import com.ues.parcial.dtos.category.CategoryUpdateDto;
import com.ues.parcial.services.CategoryService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    
    CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequestDto dto) {
        
        CategoryResponseDto responseDto = categoryService.toResponseDto(categoryService.createCategory(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryUpdateDto dto) {
        return ResponseEntity.ok(categoryService.toResponseDto(categoryService.updateCategory(id, dto)));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateCategory(@PathVariable Long id) {
        categoryService.updateCategoryStatus(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.toResponseDto(categoryService.getCategoryById(id)));
    }

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity.ok(categoryService.toResponseDtoList(categoryService.getAllCategories()));
    }
}