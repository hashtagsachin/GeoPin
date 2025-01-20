package com.geopin.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GeoCalculatorTest {
    
    @Test
    public void testDistanceCalculation() {
        // Test with known locations
        // Example: Distance from London Eye to Big Ben
        double londonEyeLat = 51.503399;
        double londonEyeLon = -0.119519;
        double bigBenLat = 51.500729;
        double bigBenLon = -0.124625;
        
        double distance = GeoCalculator.calculateDistance(
            londonEyeLat, londonEyeLon,
            bigBenLat, bigBenLon
        );
        
        // The actual distance is approximately 400 meters
        // We allow a small margin of error (10 meters)
        assertTrue(distance > 390 && distance < 410);
    }

    @Test
    public void testBoundingBox() {
        // Test point inside box
        assertTrue(GeoCalculator.isWithinBoundingBox(
            51.501, -0.120,  // point to check
            51.500, -0.125,  // southwest corner
            51.504, -0.115   // northeast corner
        ));

        // Test point outside box
        assertFalse(GeoCalculator.isWithinBoundingBox(
            51.510, -0.120,  // point outside
            51.500, -0.125,  // southwest corner
            51.504, -0.115   // northeast corner
        ));
    }
}