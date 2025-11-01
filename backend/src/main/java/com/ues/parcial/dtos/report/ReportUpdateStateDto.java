package com.ues.parcial.dtos.report;

import com.ues.parcial.Models.Enums.ReportState;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportUpdateStateDto {
    @NotNull(message = "New state cannot be null")
    ReportState newState;

    // Optional reason for state change
    String changeReason;
}
