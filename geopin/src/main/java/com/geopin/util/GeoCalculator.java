package com.geopin.util;

/**
 * Utility class for geographical calculations.
 * This class contains static methods for computing distances and checking spatial relationships
 * between points on Earth. All calculations use the haversine formula which provides good
 * approximations for real-world distances.
 */
public class GeoCalculator {
    // Earth's radius in meters - this is an average as Earth isn't perfectly spherical
    private static final double EARTH_RADIUS = 6371000;

    /**
     * Calculates the distance between two points on Earth using the haversine formula.
     * This formula provides great-circle distances between points on a sphere.
     * 
     * @param lat1 Latitude of the first point in degrees
     * @param lon1 Longitude of the first point in degrees
     * @param lat2 Latitude of the second point in degrees
     * @param lon2 Longitude of the second point in degrees
     * @return Distance between the points in meters
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Convert to radians
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Differences in coordinates
        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;

        // Haversine formula
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return EARTH_RADIUS * c; // Distance in meters
    }

    /**
     * Checks if a point lies within a rectangular bounding box.
     * The bounding box is defined by its southwest (bottom-left) and northeast (top-right) corners.
     * 
     * @param pointLat Latitude of the point to check
     * @param pointLon Longitude of the point to check
     * @param swLat Southwest corner latitude
     * @param swLon Southwest corner longitude
     * @param neLat Northeast corner latitude
     * @param neLon Northeast corner longitude
     * @return true if the point lies within the bounding box
     */
    public static boolean isWithinBoundingBox(double pointLat, double pointLon,
                                            double swLat, double swLon,
                                            double neLat, double neLon) {
        return pointLat >= swLat && pointLat <= neLat &&
               pointLon >= swLon && pointLon <= neLon;
    }
}