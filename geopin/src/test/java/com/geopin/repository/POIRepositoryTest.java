package com.geopin.repository;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import com.geopin.config.TestConfig;
import com.geopin.model.POI;
import com.geopin.model.POIStatus;
import com.geopin.model.Tag;
import java.util.List;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
		"spring.jpa.database-platform=org.hibernate.spatial.dialect.postgis.PostgisPG95Dialect",
	    "spring.jpa.hibernate.ddl-auto=create-drop",
	    "spring.jpa.show-sql=true",
	    "spring.jpa.properties.hibernate.format_sql=true"
	})

public class POIRepositoryTest {

    @Autowired
    private POIRepository poiRepository;

    @Autowired
    private TagRepository tagRepository;

    private POI centralPOI;
    private POI nearbyPOI;
    private POI farPOI;
    private Tag restaurantTag;

    @BeforeEach
    void setUp() throws ParseException {
        // Create a tag
        restaurantTag = new Tag();
        restaurantTag.setName("restaurant");
        tagRepository.save(restaurantTag);

        // Central POI: Trafalgar Square
        Point trafalgarPoint = (Point) new WKTReader().read("POINT(-0.1284 51.5080)");
        centralPOI = new POI();
        centralPOI.setName("Trafalgar Square");
        centralPOI.setLocation(trafalgarPoint);
        centralPOI.setLatitude(trafalgarPoint.getY());  // Latitude is Y coordinate
        centralPOI.setLongitude(trafalgarPoint.getX()); // Longitude is X coordinate
        centralPOI.setStatus(POIStatus.ACTIVE);         // Add status as it's required
        centralPOI.getTags().add(restaurantTag);
        poiRepository.save(centralPOI);

        // Nearby POI - Leicester Square 
        Point leicesterPoint = (Point) new WKTReader().read("POINT(-0.1283 51.5113)");
        nearbyPOI = new POI();
        nearbyPOI.setName("Leicester Square");
        nearbyPOI.setLocation(leicesterPoint);
        nearbyPOI.setLatitude(leicesterPoint.getY());
        nearbyPOI.setLongitude(leicesterPoint.getX());
        nearbyPOI.setStatus(POIStatus.ACTIVE);
        nearbyPOI.getTags().add(restaurantTag);
        poiRepository.save(nearbyPOI);

        // Far POI - St. Paul's Cathedral
        Point stPaulsPoint = (Point) new WKTReader().read("POINT(-0.0983 51.5138)");
        farPOI = new POI();
        farPOI.setName("St. Paul's Cathedral");
        farPOI.setLocation(stPaulsPoint);
        farPOI.setLatitude(stPaulsPoint.getY());
        farPOI.setLongitude(stPaulsPoint.getX());
        farPOI.setStatus(POIStatus.ACTIVE);
        poiRepository.save(farPOI);
    }

    @Test
    void testFindPOIsWithinDistance() throws ParseException {
        // Test point: Trafalgar Square
        Point center = (Point) new WKTReader().read("POINT(-0.1284 51.5080)");
        
        // Search within 1 km
        List<POI> poisWithin1km = poiRepository.findPOIsWithinDistance(center, 1000.0); // Note: using meters
        assertEquals(2, poisWithin1km.size(), 
            "Should find 2 POIs within 1km of Trafalgar Square");
        assertTrue(poisWithin1km.contains(centralPOI),
            "Trafalgar Square should be included in 1km radius");
        assertTrue(poisWithin1km.contains(nearbyPOI),
            "Leicester Square should be included in 1km radius");
        assertFalse(poisWithin1km.contains(farPOI),
            "St. Paul's Cathedral should not be included in 1km radius");

        // Search within 3 km (should include all POIs)
        List<POI> poisWithin3km = poiRepository.findPOIsWithinDistance(center, 3000.0);
        assertEquals(3, poisWithin3km.size(),
            "Should find all 3 POIs within 3km of Trafalgar Square");
    }

    @Test
    void testFindPOIsWithinBoundingBox() throws ParseException {
        // Create a bounding box around Trafalgar Square and Leicester Square
        // but excluding St. Paul's Cathedral
        Point southWest = (Point) new WKTReader().read("POINT(-0.1300 51.5070)");
        Point northEast = (Point) new WKTReader().read("POINT(-0.1270 51.5120)");
        
        List<POI> poisInBox = poiRepository.findPOIsWithinBoundingBox(southWest, northEast);
        assertEquals(2, poisInBox.size(), 
            "Should find 2 POIs within the West End bounding box");
        assertTrue(poisInBox.contains(centralPOI), 
            "Trafalgar Square should be within the bounding box");
        assertTrue(poisInBox.contains(nearbyPOI), 
            "Leicester Square should be within the bounding box");
        assertFalse(poisInBox.contains(farPOI), 
            "St. Paul's Cathedral should be outside the bounding box");
    }

    @Test
    void testFindPOIsByTagName() {
        // Use tagRepository instead of poiRepository for tag-based queries
        List<POI> restaurantPOIs = tagRepository.findPOIsByTagName("restaurant");
        assertEquals(2, restaurantPOIs.size(), 
            "Should find 2 POIs tagged as restaurants");
        assertTrue(restaurantPOIs.contains(centralPOI),
            "Trafalgar Square should be included in restaurant POIs");
        assertTrue(restaurantPOIs.contains(nearbyPOI),
            "Leicester Square should be included in restaurant POIs");
        assertFalse(restaurantPOIs.contains(farPOI),
            "St. Paul's Cathedral should not be included in restaurant POIs");
    }

    @Test
    void testFindPOIsByNonExistentTag() {
        // Use tagRepository instead of poiRepository for tag-based queries
        List<POI> nonExistentTagPOIs = tagRepository.findPOIsByTagName("nonexistent");
        assertTrue(nonExistentTagPOIs.isEmpty(),
            "Should return empty list for non-existent tag");
    }
    
    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> TestConfig.postgres.getJdbcUrl());
        registry.add("spring.datasource.username", () -> TestConfig.postgres.getUsername());
        registry.add("spring.datasource.password", () -> TestConfig.postgres.getPassword());
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
    }
}