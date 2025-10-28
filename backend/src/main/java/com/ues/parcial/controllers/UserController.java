package com.ues.parcial.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ues.parcial.dtos.user.CreateUserDto;
import com.ues.parcial.dtos.user.UpdatePhoneDto;
import com.ues.parcial.dtos.user.UpdateRoleDto;
import com.ues.parcial.dtos.user.UserResponseDto;
import com.ues.parcial.services.UserService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid CreateUserDto dto) {
        return ResponseEntity.ok(userService.toResponseDto(userService.createUser(dto)));
    }

    @PatchMapping("/{id}/phone")
    public ResponseEntity<UserResponseDto> updatePhone(@PathVariable String id, @RequestBody @Valid UpdatePhoneDto dto) {
        return ResponseEntity.ok(userService.toResponseDto(userService.updatePhone(id, dto.getPhone())));
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<UserResponseDto> updateUserRole(@PathVariable String id, @RequestBody @Valid UpdateRoleDto dto) {
        return ResponseEntity.ok(userService.toResponseDto(userService.updateUserRole(id, dto.getRole())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.toResponseDto(userService.getUserById(id)));
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.toResponseDto(userService.getAllUsers()));
    }
}