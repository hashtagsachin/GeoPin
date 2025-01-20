package com.geopin.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for GeoCalculator utility.
 * This class verifies the accuracy of geographical calculations used in the application.
 */
public class GeoCalculatorTest {

    /**
     * Tests distance calculation between two well-known London landmarks.
     * The London Eye and Big Ben are ideal test cases as they:
     * 1. Have well-documented coordinates
     * 2. Are close enough that Earth's curvature has minimal impact
     * 3. Have a clear line of sight between them
     */
    @Test
    @DisplayName("Calculate distance between London Eye and Big Ben")
    public void testDistanceCalculationWithLandmarks() {
        // London Eye coordinates (highly accurate from survey data)
        double londonEyeLat = 51.503399;
        double londonEyeLon = -0.119519;
        
        // Big Ben coordinates (highly accurate from survey data)
        double bigBenLat = 51.500729;
        double bigBenLon = -0.124625;
        
        double distance = GeoCalculator.calculateDistance(
            londonEyeLat, londonEyeLon,
            bigBenLat, bigBenLon
        );
        
        // The actual direct (as-the-crow-flies) distance is approximately 460 meters
        // We allow a small margin of error (10 meters) to account for:
        // - Slight variations in coordinate precision
        // - Mathematical approximations in the haversine formula
        double expectedDistance = 460.0;
        double allowedError = 10.0;
        
        assertTrue(Math.abs(distance - expectedDistance) <= allowedError,
                  String.format("Distance was %.2f meters, expected %.2f ± %.2f meters", 
                              distance, expectedDistance, allowedError));
    }

    /**
     * Tests distance calculation when points are identical.
     * Uses the London Eye's coordinates as our reference point.
     */
    @Test
    @DisplayName("Calculate distance between identical points")
    public void testDistanceWithSamePoint() {
        // London Eye coordinates
        double lat = 51.503399;
        double lon = -0.119519;
        
        double distance = GeoCalculator.calculateDistance(lat, lon, lat, lon);
        
        // When points are identical, distance should be effectively zero
        // We allow a tiny margin (0.1m) for floating-point arithmetic
        assertEquals(0.0, distance, 0.1,
                    "Distance between identical points should be zero");
    }

    /**
     * Tests bounding box check with central London landmarks.
     * Uses the London Eye as our test point within a box covering central London.
     */
    @Test
    @DisplayName("Check point within central London bounding box")
    public void testPointInsideBoundingBox() {
        // Point: London Eye
        double pointLat = 51.503399;
        double pointLon = -0.119519;
        
        // Bounding box: Central London area covering major landmarks
        double swLat = 51.500000;  // Southwest corner
        double swLon = -0.125000;
        double neLat = 51.505000;  // Northeast corner
        double neLon = -0.115000;
        
        assertTrue(GeoCalculator.isWithinBoundingBox(
            pointLat, pointLon,
            swLat, swLon,
            neLat, neLon
        ), "London Eye should be within central London bounding box");
    }

    /**
     * Tests bounding box check with a point clearly outside central London.
     * Uses Heathrow Airport as our test point outside the central London box.
     */
    @Test
    @DisplayName("Check point outside central London bounding box")
    public void testPointOutsideBoundingBox() {
        // Point: Heathrow Airport (well outside central London)
        double pointLat = 51.470020;
        double pointLon = -0.454295;
        
        // Bounding box: Central London area (same as previous test)
        double swLat = 51.500000;
        double swLon = -0.125000;
        double neLat = 51.505000;
        double neLon = -0.115000;
        
        assertFalse(GeoCalculator.isWithinBoundingBox(
            pointLat, pointLon,
            swLat, swLon,
            neLat, neLon
        ), "Heathrow Airport should be outside central London bounding box");
    }
}