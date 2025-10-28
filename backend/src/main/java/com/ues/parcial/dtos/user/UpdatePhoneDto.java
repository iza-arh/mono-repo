package com.ues.parcial.dtos.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePhoneDto {
    @NotBlank(message = "Phone cannot be blank")
    @Pattern(
        regexp = "^(\\d{8}|\\d{4}-\\d{4})$",
        message = "Invalid phone number format. Expected: 12345678 or 1234-5678"
    )
    private String phone;
}
