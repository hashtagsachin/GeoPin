# Testing Strategy Documentation for GeoPin

## Overview
This document outlines the comprehensive testing strategy implemented for the GeoPin application, focusing particularly on the spatial data handling capabilities. Our testing approach emphasizes reliability, reproducibility, and real-world applicability using actual geographic coordinates from London landmarks.

## Testing Environment Setup

### Docker and TestContainers
We use Docker through TestContainers to ensure consistent and isolated testing environments. This approach offers several key advantages:

1. **Clean State Guarantee**: Each test run begins with a fresh database instance, eliminating test interdependencies and data pollution.
2. **Consistent Environment**: All developers work with identical database configurations, preventing "works on my machine" scenarios.
3. **PostGIS Integration**: Our Docker container (postgis/postgis:15-3.3) comes pre-configured with PostGIS extensions, ensuring proper spatial functionality testing.
4. **Automatic Resource Management**: TestContainers handles container lifecycle management, including cleanup after tests complete.

### Test Configuration
Our test configuration is structured in two main components:

```java
@TestConfiguration
public class TestConfig {
    public static final PostgreSQLContainer<?> postgres;

    static {
        // Configure PostgreSQL container with PostGIS support
        postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgis/postgis:15-3.3"))
            .withDatabaseName("geopin_test")
            .withUsername("test")
            .withPassword("test")
            .withCommand("postgres -c max_prepared_transactions=100");
        
        postgres.start();
    }
}
```

This configuration ensures that our tests run against a properly configured PostGIS-enabled database.

## Test Data Design
Our test data is carefully chosen to represent real-world scenarios using actual London landmarks:

1. **Central Point**: Trafalgar Square (51.5080° N, 0.1284° W)
2. **Nearby Point**: Leicester Square (51.5113° N, 0.1283° W)
3. **Distant Point**: St. Paul's Cathedral (51.5138° N, 0.0983° W)

This choice of locations provides several advantages:
- Known, verifiable distances between points
- Real-world coordinates for testing actual GPS data handling
- Familiar landmarks that make test scenarios more intuitive

## Test Cases

### 1. Distance-Based Search Testing
```java
@Test
void testFindPOIsWithinDistance() {
    Point center = (Point) new WKTReader().read("POINT(-0.1284 51.5080)");
    
    // Test 1km radius (should include Trafalgar Square and Leicester Square)
    List<POI> poisWithin1km = poiRepository.findPOIsWithinDistance(center, 1000.0);
    assertEquals(2, poisWithin1km.size(), 
        "Should find 2 POIs within 1km of Trafalgar Square");

    // Test 3km radius (should include all points)
    List<POI> poisWithin3km = poiRepository.findPOIsWithinDistance(center, 3000.0);
    assertEquals(3, poisWithin3km.size(),
        "Should find all 3 POIs within 3km of Trafalgar Square");
}
```

This test verifies our spatial distance calculations using real-world distances in central London.

### 2. Bounding Box Search Testing
```java
@Test
void testFindPOIsWithinBoundingBox() {
    Point southWest = (Point) new WKTReader().read("POINT(-0.1300 51.5070)");
    Point northEast = (Point) new WKTReader().read("POINT(-0.1270 51.5120)");
    
    List<POI> poisInBox = poiRepository.findPOIsWithinBoundingBox(southWest, northEast);
    assertEquals(2, poisInBox.size(), 
        "Should find 2 POIs within the West End bounding box");
}
```

This test confirms our ability to find POIs within a geographic rectangle, useful for map viewport queries.

### 3. Tag-Based Search Testing
We implement two complementary tests for tag functionality:

```java
@Test
void testFindPOIsByTagName() {
    List<POI> restaurantPOIs = tagRepository.findPOIsByTagName("restaurant");
    assertEquals(2, restaurantPOIs.size(), 
        "Should find 2 POIs tagged as restaurants");
}

@Test
void testFindPOIsByNonExistentTag() {
    List<POI> nonExistentTagPOIs = tagRepository.findPOIsByTagName("nonexistent");
    assertTrue(nonExistentTagPOIs.isEmpty(),
        "Should return empty list for non-existent tag");
}
```

These tests verify both positive and negative scenarios in our tagging system.

## Performance Considerations
Our test execution times provide insight into query performance:
- Tag queries: ~0.012s
- Bounding box queries: ~0.018s
- Distance queries: ~0.090s

These timings suggest efficient spatial index utilization, crucial for production performance.

## Future Testing Considerations

1. **Edge Cases**:
   - International Date Line crossing
   - Polar region coordinates
   - Extremely dense POI clusters

2. **Performance Testing**:
   - Large dataset behavior
   - Concurrent query handling
   - Index efficiency analysis

3. **Integration Testing**:
   - Frontend map interaction
   - Real-time update scenarios
   - Mobile device location accuracy

## Conclusion
Our testing strategy provides a robust foundation for ensuring GeoPin's spatial functionality works correctly. The combination of Docker-based isolation, real-world test data, and comprehensive test cases gives us confidence in the application's reliability and performance.