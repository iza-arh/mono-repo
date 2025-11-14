package com.ues.parcial.dtos.report;

import java.util.UUID;
import com.ues.parcial.dtos.category.CategorySimpleDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportSimpleDto {
    
    private UUID id;
    private String title;
    private CategorySimpleDto categorySimpleDto;
}
