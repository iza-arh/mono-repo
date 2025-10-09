package com.ues.parcial.dtos.category;

import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryAdminResposeDto {
    private Long id;
    private String code;
    private String name;
    private boolean isActive;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
