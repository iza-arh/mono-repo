package com.ues.parcial.utils;

import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

// Utility class for creating geometrical objects.
// Currently supports creating Points from longitude and latitude.
// All geometries are created with SRID 4326 (WGS84).
public class GeometryUtils {
    // Shared GeometryFactory instance with WGS84 SRID, WGS 84 is a standard for GPS coordinates.
    private static final GeometryFactory GEOM_FACTORY = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326); 

    public static Point pointFromLonLat(double lon, double lat) {
        Point p = GEOM_FACTORY.createPoint(new Coordinate(lon, lat));
        p.setSRID(4326);
        return p;
    }

    // if [lon, lat] is recived as List<Double>
    public static Point pointFromList(List<Double> point) {
        if (point == null || point.size() < 2) throw new IllegalArgumentException("Point must be [lon, lat]");
        return pointFromLonLat(point.get(0), point.get(1));
    }
}
