package com.geopin.controller;

import com.geopin.model.POI;
import com.geopin.service.POIService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for managing Points of Interest (POIs).
 * This controller handles HTTP requests related to POIs, including creation,
 * retrieval, updates, deletion, and spatial searches.
 * 
 * The @RestController annotation combines @Controller and @ResponseBody,
 * indicating that this class will handle REST requests and responses.
 * 
 * @RequestMapping("/api/pois") means all endpoints in this controller
 * will start with /api/pois
 */
@RestController
@RequestMapping("/api/pois")
public class POIController {
    
    private final POIService poiService;
    
    /**
     * Constructor injection of POIService.
     * Spring Boot will automatically wire the POIService implementation.
     * Constructor injection is preferred over field injection as it
     * ensures required dependencies are provided at creation time.
     */
    @Autowired
    public POIController(POIService poiService) {
        this.poiService = poiService;
    }
    
    /**
     * Creates a new POI.
     * 
     * @param poi The POI object to create, provided in request body
     * @return ResponseEntity containing the created POI
     */
    @PostMapping
    public ResponseEntity<POI> createPOI(@RequestBody POI poi) {
        POI savedPOI = poiService.savePOI(poi);
        return ResponseEntity.ok(savedPOI);
    }
    
    /**
     * Retrieves all POIs.
     * 
     * @return ResponseEntity containing a list of all POIs
     */
    @GetMapping
    public ResponseEntity<List<POI>> getAllPOIs() {
        List<POI> pois = poiService.getAllPOIs();
        return ResponseEntity.ok(pois);
    }
    
    /**
     * Retrieves a specific POI by its ID.
     * 
     * @param id The ID of the POI to retrieve
     * @return ResponseEntity containing the requested POI
     */
    @GetMapping("/{id}")
    public ResponseEntity<POI> getPOIById(@PathVariable Long id) {
        POI poi = poiService.getPOIById(id);
        return ResponseEntity.ok(poi);
    }
    
    /**
     * Updates an existing POI.
     * 
     * @param id The ID of the POI to update
     * @param poi The updated POI data
     * @return ResponseEntity containing the updated POI
     */
    @PutMapping("/{id}")
    public ResponseEntity<POI> updatePOI(@PathVariable Long id, @RequestBody POI poi) {
        POI updatedPOI = poiService.updatePOI(id, poi);
        return ResponseEntity.ok(updatedPOI);
    }
    
    /**
     * Deletes a POI.
     * 
     * @param id The ID of the POI to delete
     * @return ResponseEntity with no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePOI(@PathVariable Long id) {
        poiService.deletePOI(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Finds POIs within a specified distance from a central point.
     * This endpoint uses the Haversine formula (through the service layer)
     * to calculate real-world distances between points.
     * 
     * @param lat Latitude of the center point
     * @param lng Longitude of the center point
     * @param distanceInMeters Radius to search within (in meters)
     * @return ResponseEntity containing a list of POIs within the specified distance
     */
    @GetMapping("/search/radius")
    public ResponseEntity<List<POI>> findPOIsWithinDistance(
            @RequestParam("lat") double lat,
            @RequestParam("lng") double lng,
            @RequestParam("distance") double distanceInMeters) {
        List<POI> pois = poiService.findPOIsWithinDistance(lat, lng, distanceInMeters);
        return ResponseEntity.ok(pois);
    }

    /**
     * Finds POIs within a rectangular bounding box.
     * The box is defined by its southwest (bottom-left) and northeast (top-right) corners.
     * This is useful for finding POIs within the current map view in the frontend.
     * 
     * @param swLat Latitude of the southwest corner
     * @param swLng Longitude of the southwest corner
     * @param neLat Latitude of the northeast corner
     * @param neLng Longitude of the northeast corner
     * @return ResponseEntity containing a list of POIs within the bounding box
     */
    @GetMapping("/search/bounds")
    public ResponseEntity<List<POI>> findPOIsWithinBoundingBox(
            @RequestParam("swLat") double swLat,
            @RequestParam("swLng") double swLng,
            @RequestParam("neLat") double neLat,
            @RequestParam("neLng") double neLng) {
        List<POI> pois = poiService.findPOIsWithinBoundingBox(
            swLat, swLng, neLat, neLng);
        return ResponseEntity.ok(pois);
    }
}