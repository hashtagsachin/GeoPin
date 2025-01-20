package com.geopin.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;

import com.geopin.model.POI;
import java.util.List;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class POIServiceTest {

    @Autowired
    private POIService poiService;

    @Test
    public void testFindPOIsWithinDistance() {
        // Create some test POIs
        POI londonEye = new POI("London Eye", 51.503399, -0.119519);
        POI bigBen = new POI("Big Ben", 51.500729, -0.124625);
        POI stPauls = new POI("St Paul's", 51.513870, -0.098362);
        
        poiService.savePOI(londonEye);
        poiService.savePOI(bigBen);
        poiService.savePOI(stPauls);

        // Search within 500 meters of London Eye
        List<POI> nearbyPOIs = poiService.findPOIsWithinDistance(
            51.503399, -0.119519, 500
        );

        // Should find London Eye and Big Ben, but not St Paul's
        assertEquals(2, nearbyPOIs.size());
        assertTrue(nearbyPOIs.stream()
            .anyMatch(poi -> poi.getName().equals("London Eye")));
        assertTrue(nearbyPOIs.stream()
            .anyMatch(poi -> poi.getName().equals("Big Ben")));
    }

    @Test
    public void testBasicCRUDOperations() {
        // Test Create
        POI newPOI = new POI("Test Location", 51.5074, -0.1278);
        POI saved = poiService.savePOI(newPOI);
        assertNotNull(saved.getId());

        // Test Read
        POI found = poiService.getPOIById(saved.getId());
        assertEquals("Test Location", found.getName());

        // Test Update
        found.setName("Updated Location");
        POI updated = poiService.updatePOI(found.getId(), found);
        assertEquals("Updated Location", updated.getName());

        // Test Delete
        poiService.deletePOI(updated.getId());
        // Should throw ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () -> {
            poiService.getPOIById(updated.getId());
        });
    }
}