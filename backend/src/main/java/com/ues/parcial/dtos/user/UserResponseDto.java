package com.ues.parcial.dtos.user;

import com.ues.parcial.Models.Enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private String id;
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private UserRole role;
}
