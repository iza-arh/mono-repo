package com.ues.parcial.mappers.report;

import com.ues.parcial.Models.Category;
import com.ues.parcial.Models.Report;
import com.ues.parcial.Models.User;
import com.ues.parcial.Models.Zone;
import com.ues.parcial.dtos.category.CategorySimpleDto;
import com.ues.parcial.dtos.report.ReportRequestDto;
import com.ues.parcial.dtos.report.ReportResponseDto;
import com.ues.parcial.dtos.report.ReportUpdateDto;
import com.ues.parcial.dtos.user.UserSimpleDto;
import com.ues.parcial.dtos.zone.GeometryDto;
import com.ues.parcial.dtos.zone.ZoneSimpleDto;
import com.ues.parcial.exceptions.InvalidGeometryException;
import com.ues.parcial.exceptions.ResourceNotFoundException;
import com.ues.parcial.repositories.CategoryRepository;
import com.ues.parcial.repositories.UserRepository;
import com.ues.parcial.repositories.ZoneRepository;
import com.ues.parcial.utils.GeometryUtils;
import org.locationtech.jts.geom.Point;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", imports = {GeometryUtils.class})
public abstract class ReportMapper {

    @Autowired
    protected CategoryRepository categoryRepository;
    @Autowired
    protected ZoneRepository zoneRepository;
    @Autowired
    protected UserRepository userRepository;

    // ---------------------------------------------------------
    // 1. CREATE: From DTO to Entity
    // ---------------------------------------------------------
    // This filds are ignored because they are auto-generated or set by the system
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "active", ignore = true) 
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "priority", ignore = true)

    // This fields are inverse relationship fields that we ignore when creating
    @Mapping(target = "reportPhotos", ignore = true)
    @Mapping(target = "assignments", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "reportHistories", ignore = true)
    @Mapping(target = "duplicateOf", ignore = true)
    
    // Custom mappings
    @Mapping(target = "locationText", source = "locationText") 
    @Mapping(target = "category", source = "categoryId", qualifiedByName = "mapCategoryById")
    @Mapping(target = "zone", source = "zoneId", qualifiedByName = "mapZoneById")
    @Mapping(target = "reporter", source = "reporterId", qualifiedByName = "mapReporterById")
    @Mapping(target = "location", source = "geom", qualifiedByName = "mapGeometryFromDto")
    @Mapping(target = "title", expression = "java(dto.getTitle() != null ? dto.getTitle().trim() : null)")
    public abstract Report toEntity(ReportRequestDto dto);

    // ---------------------------------------------------------
    // 2. UPDATE: from DTO to an existing Entity
    // ---------------------------------------------------------
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE) // Ignore null values in the DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reporter", ignore = true) 
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    
    @Mapping(target = "reportPhotos", ignore = true)
    @Mapping(target = "assignments", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "reportHistories", ignore = true)
    @Mapping(target = "duplicateOf", ignore = true)
    @Mapping(target = "active", ignore = true) 

    @Mapping(target = "category", source = "categoryId", qualifiedByName = "mapCategoryById")
    @Mapping(target = "zone", source = "zoneId", qualifiedByName = "mapZoneById")
    @Mapping(target = "location", source = "geom", qualifiedByName = "mapGeometryFromDto")
    @Mapping(target = "title", expression = "java(dto.getTitle() != null ? dto.getTitle().trim() : report.getTitle())")
    public abstract void updateEntityFromDto(ReportUpdateDto dto, @MappingTarget Report report);

    // ---------------------------------------------------------
    // 3. READ: From Entity to Response DTO 
    // ---------------------------------------------------------
    @Mapping(target = "categoryId", source = "category", qualifiedByName = "mapCategoryToSimpleDto")
    @Mapping(target = "zoneId", source = "zone", qualifiedByName = "mapZoneToSimpleDto")
    @Mapping(target = "reporter", source = "reporter", qualifiedByName = "mapUserToSimpleDto")
    @Mapping(target = "geom", source = "location", qualifiedByName = "mapGeometryToDto")
    public abstract ReportResponseDto toResponseDto(Report report);

    // This method returns a list mapping
    public abstract List<ReportResponseDto> toResponseDto(List<Report> reports);

    // ---------------------------------------------------------
    // HELPERS FOR "TO ENTITY" 
    // ---------------------------------------------------------
    @Named("mapCategoryById")
    protected Category mapCategoryById(Long id) {
        if (id == null) return null;
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    @Named("mapZoneById")
    protected Zone mapZoneById(UUID id) {
        if (id == null) return null;
        return zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));
    }

    @Named("mapReporterById")
    protected User mapReporterById(String id) {
        if (id == null) return null;
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reporter not found"));
    }

    @Named("mapGeometryFromDto")
    protected Point mapGeometryFromDto(GeometryDto geom) {
        if (geom == null) return null;
        if ("Point".equalsIgnoreCase(geom.getType())) {
            return GeometryUtils.pointFromList(geom.getPoint());
        }
        throw new InvalidGeometryException("Only Point geometry supported");
    }

    // ---------------------------------------------------------
    // HELPERS FOR "TO RESPONSE DTO"
    // ---------------------------------------------------------
    
    @Named("mapCategoryToSimpleDto")
    protected CategorySimpleDto mapCategoryToSimpleDto(Category category) {
        if (category == null) return null;
        CategorySimpleDto dto = new CategorySimpleDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }

    @Named("mapZoneToSimpleDto")
    protected ZoneSimpleDto mapZoneToSimpleDto(Zone zone) {
        if (zone == null) return null;
        ZoneSimpleDto dto = new ZoneSimpleDto();
        dto.setId(zone.getId());
        dto.setName(zone.getName());
        return dto;
    }

    @Named("mapUserToSimpleDto")
    protected UserSimpleDto mapUserToSimpleDto(User user) {
        if (user == null) return null;
        UserSimpleDto dto = new UserSimpleDto();
        dto.setName(user.getName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        return dto;
    }

    @Named("mapGeometryToDto")
    protected GeometryDto mapGeometryToDto(Point point) {
        if (point == null) return null;
        GeometryDto dto = new GeometryDto();
        dto.setType("Point");
        dto.setPoint(Arrays.asList(point.getX(), point.getY()));
        return dto;
    }
}