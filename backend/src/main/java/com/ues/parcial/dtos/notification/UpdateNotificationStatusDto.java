package com.ues.parcial.dtos.notification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateNotificationStatusDto {

    @NotBlank(message = "Status cannot be blank")
    @Pattern(
        regexp = "PENDING|SENT|FAILED|READ",
        flags = Pattern.Flag.CASE_INSENSITIVE,
        message = "Status must be one of: PENDING, SENT, FAILED, READ"
    )
    private String status;
}
