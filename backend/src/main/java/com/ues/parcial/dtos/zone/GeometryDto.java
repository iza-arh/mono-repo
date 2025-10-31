package com.ues.parcial.dtos.zone;

import lombok.Data;
import java.util.List;

    /*
     * Represents a geometry in GeoJSON format for data transfer.
     * Example Polygon coordinates structure:
     * 
    *   [
    *       [ [lon1, lat1], [lon2, lat2], [lon3, lat3], [lon1, lat1] 
    *   ]
     */

@Data
public class GeometryDto {
    private String type; // "Point" or "Polygon"
    private List<List<List<Double>>> coordinates; // for Polygon
    private List<Double> point; // for Point: [lon, lat]
}