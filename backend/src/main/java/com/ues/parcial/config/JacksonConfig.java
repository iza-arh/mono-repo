package com.ues.parcial.config;

import org.n52.jackson.datatype.jts.JtsModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*Jackson configuration for handling JTS (Java Topology Suite) geometry types.
 * 
 *  This configuration registers the JTS module with Jackson to enable:
 * - Serialization: Converting JTS Geometry objects (Point, Polygon, etc.) to GeoJSON
 * - Deserialization: Converting GeoJSON to JTS Geometry objects
 */

@Configuration
public class JacksonConfig {

    @Bean
    public JtsModule jtsModule() {
        return new JtsModule();
    }
}