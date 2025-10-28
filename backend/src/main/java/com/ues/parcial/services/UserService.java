package com.ues.parcial.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ues.parcial.Models.User;
import com.ues.parcial.Models.Enums.UserRole;
import com.ues.parcial.dtos.user.CreateUserDto;
import com.ues.parcial.dtos.user.UserResponseDto;
import com.ues.parcial.exceptions.EmailMismatchException;
import com.ues.parcial.exceptions.IdMismatchException;
import com.ues.parcial.exceptions.PhoneAlreadyInUseException;
import com.ues.parcial.exceptions.ResourceNotFoundException;
import com.ues.parcial.repositories.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Creates a new user if one with the given email does not already exist or returns the existing user.
    @Transactional
    public User createUser(CreateUserDto dto) {
        Optional<User> existingById = userRepository.findById(dto.getId());
        Optional<User> existingByEmail = userRepository.findByEmailIgnoreCase(dto.getEmail());

        // Case: Both ID and email exist
        if (existingById.isPresent()) {
            User userById = existingById.get();
            if (!dto.getEmail().equalsIgnoreCase(userById.getEmail())) {
                throw new EmailMismatchException("Email does not match existing user ID");
            }
            return userById; // ID and email match, return existing user
        }

        // Case: Email exists but ID does not match
        if (existingByEmail.isPresent()) {
            throw new IdMismatchException("ID does not match existing email");
        }

        // Case: Neither ID nor email exist; create a new user
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(User.validatePhone(dto.getPhone())); // optional field
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getUserById(String id) {
        return userRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional
    public User updatePhone(String userId, String newPhone) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        userRepository.findByPhone(User.validatePhone(newPhone))
                    .filter(u -> !u.getId().equals(userId)) // Ensure it's not the same user
                    .ifPresent(u -> { throw new PhoneAlreadyInUseException("Phone number is already in use"); });

        user.setPhone(newPhone);
        return userRepository.save(user);
    }

    @Transactional
    public User updateUserRole(String userId, String newRoleStr) {
        User user = userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserRole newRole = UserRole.valueOf(newRoleStr);
        user.setRole(newRole);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findByIsActiveTrue();
    }

    public UserResponseDto toResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        return dto;
    }

    public List<UserResponseDto> toResponseDto(List<User> users){
        return users.stream().map(this::toResponseDto).toList();
    }
}