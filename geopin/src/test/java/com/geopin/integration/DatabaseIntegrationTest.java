package com.geopin.integration;

import com.geopin.model.POI;
import com.geopin.model.POIStatus;
import com.geopin.model.Tag;
import com.geopin.repository.POIRepository;
import com.geopin.repository.TagRepository;
import com.geopin.service.POIService;
import com.geopin.util.GeoCalculator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class DatabaseIntegrationTest {

    @Autowired
    private POIRepository poiRepository;

    @Autowired
    private POIService poiService;
    
    @Autowired
    private TagRepository tagRepository;
    
    
    @Test
    void testBasicPOIOperations() {
        // Create a new POI
        POI trafalgarSquare = new POI();
        trafalgarSquare.setName("Trafalgar Square");
        trafalgarSquare.setLatitude(51.5080);
        trafalgarSquare.setLongitude(-0.1284);
        trafalgarSquare.setDescription("Famous London square with Nelson's Column");
        trafalgarSquare.setStatus(POIStatus.ACTIVE);

        // Save it to the database
        POI savedPOI = poiRepository.save(trafalgarSquare);
        
        // Verify it was saved correctly
        assertNotNull(savedPOI.getId(), "POI should have been assigned an ID");
        assertEquals("Trafalgar Square", savedPOI.getName(), "POI name should match");
        assertEquals(51.5080, savedPOI.getLatitude(), 0.0001, "Latitude should match");
        assertEquals(-0.1284, savedPOI.getLongitude(), 0.0001, "Longitude should match");
    }
    
    
    
    
    
    @Test
    void testGeographicalQueries() {
        // Create two POIs with known distances
        POI trafalgarSquare = new POI();
        trafalgarSquare.setName("Trafalgar Square");
        trafalgarSquare.setLatitude(51.5080);
        trafalgarSquare.setLongitude(-0.1284);
        trafalgarSquare.setStatus(POIStatus.ACTIVE);
        poiRepository.save(trafalgarSquare);

        POI leicesterSquare = new POI();
        leicesterSquare.setName("Leicester Square");
        leicesterSquare.setLatitude(51.5113);
        leicesterSquare.setLongitude(-0.1283);
        leicesterSquare.setStatus(POIStatus.ACTIVE);
        poiRepository.save(leicesterSquare);

        // Test finding POIs within 500 meters of Trafalgar Square
        List<POI> nearbyPOIs = poiService.findPOIsWithinDistance(51.5080, -0.1284, 500.0);
        
        assertEquals(2, nearbyPOIs.size(), "Should find both squares within 500m");
        assertTrue(nearbyPOIs.stream().anyMatch(poi -> poi.getName().equals("Leicester Square")),
                "Should find Leicester Square nearby");
    }
    
    
    
    
    @Test
    void testStatusFiltering() {
        // Create POIs with different statuses
        POI activePOI = new POI();
        activePOI.setName("Active Location");
        activePOI.setLatitude(51.5080);
        activePOI.setLongitude(-0.1284);
        activePOI.setStatus(POIStatus.ACTIVE);
        poiRepository.save(activePOI);

        POI temporaryPOI = new POI();
        temporaryPOI.setName("Temporary Location");
        temporaryPOI.setLatitude(51.5113);
        temporaryPOI.setLongitude(-0.1283);
        temporaryPOI.setStatus(POIStatus.TEMPORARY);
        poiRepository.save(temporaryPOI);

        // Test filtering by status
        List<POI> activePOIs = poiRepository.findByStatus(POIStatus.ACTIVE);
        List<POI> temporaryPOIs = poiRepository.findByStatus(POIStatus.TEMPORARY);

        assertEquals(1, activePOIs.size(), "Should find one active POI");
        assertEquals(1, temporaryPOIs.size(), "Should find one temporary POI");
        assertEquals("Active Location", activePOIs.get(0).getName(), "Should find correct active POI");
        assertEquals("Temporary Location", temporaryPOIs.get(0).getName(), "Should find correct temporary POI");
    }
    
    
    
    
    
    
    @Test
    void testTagOperations() {
        // Create a POI
        POI trafalgarSquare = new POI();
        trafalgarSquare.setName("Trafalgar Square");
        trafalgarSquare.setLatitude(51.5080);
        trafalgarSquare.setLongitude(-0.1284);
        trafalgarSquare.setStatus(POIStatus.ACTIVE);

        // Create some tags
        Tag tourismTag = new Tag();
        tourismTag.setName("tourism");
        
        Tag landmarkTag = new Tag();
        landmarkTag.setName("landmark");
        
        // Save tags first
        tourismTag = tagRepository.save(tourismTag);
        landmarkTag = tagRepository.save(landmarkTag);
        
        // Add tags to POI
        trafalgarSquare.addTag(tourismTag);
        trafalgarSquare.addTag(landmarkTag);
        
        // Save POI with tags
        POI savedPOI = poiRepository.save(trafalgarSquare);
        
        // Verify tags were saved correctly
        POI retrievedPOI = poiRepository.findById(savedPOI.getId()).orElseThrow();
        assertEquals(2, retrievedPOI.getTags().size(), "Should have 2 tags");
        assertTrue(
            retrievedPOI.getTags().stream()
                .map(Tag::getName)
                .anyMatch(name -> name.equals("tourism")),
            "Should have tourism tag"
        );
        assertTrue(
            retrievedPOI.getTags().stream()
                .map(Tag::getName)
                .anyMatch(name -> name.equals("landmark")),
            "Should have landmark tag"
        );
        
        // Test finding POI by tag
        List<POI> tourismPOIs = poiRepository.findByTags_NameContainingIgnoreCase("tourism");
        assertEquals(1, tourismPOIs.size(), "Should find one POI with tourism tag");
        assertEquals("Trafalgar Square", tourismPOIs.get(0).getName());
    }
    
}