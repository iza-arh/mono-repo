package com.ues.parcial.dtos.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRoleDto {
    @NotNull(message = "Role cannot be null")
    @Pattern(
        regexp = "CITIZEN|TECHNICIAN|MANAGER|ADMIN",
        message = "Role must be one of: CITIZEN, TECHNICIAN, MANAGER, ADMIN (in uppercase)"
    )
    private String role;
}
