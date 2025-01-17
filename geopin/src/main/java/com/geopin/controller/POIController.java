package com.geopin.controller;

import com.geopin.model.POI;
import com.geopin.service.POIService;

import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

@RestController
@RequestMapping("/api/pois") //all endpoints start with /api/pois
public class POIController {
	
	private final POIService poiService;
	
	@Autowired
    public POIController(POIService poiService) {
        this.poiService = poiService;
    }
	
	@PostMapping
    public ResponseEntity<POI> createPOI(@RequestBody POI poi) {
        POI savedPOI = poiService.savePOI(poi);
        return ResponseEntity.ok(savedPOI);
    }
	
	@GetMapping
    public ResponseEntity<List<POI>> getAllPOIs() {
        List<POI> pois = poiService.getAllPOIs();
        return ResponseEntity.ok(pois);
    }
	
	@GetMapping("/{id}")
    public ResponseEntity<POI> getPOIById(@PathVariable Long id) {
        POI poi = poiService.getPOIById(id);
        return ResponseEntity.ok(poi);
    }
	
	@PutMapping("/{id}")
    public ResponseEntity<POI> updatePOI(@PathVariable Long id, @RequestBody POI poi) {
        POI updatedPOI = poiService.updatePOI(id, poi);
        return ResponseEntity.ok(updatedPOI);
    }
	
	@DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePOI(@PathVariable Long id) {
        poiService.deletePOI(id);
        return ResponseEntity.noContent().build();
    }
	
	
	
	
	@GetMapping("/search/radius")
    public ResponseEntity<List<POI>> findPOIsWithinDistance(
            @RequestParam("lat") double lat,
            @RequestParam("lng") double lng,
            @RequestParam("distance") double distanceInMeters) {
        
        // Create a Point from the latitude and longitude
        GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);
        Point center = gf.createPoint(new Coordinate(lng, lat));
        
        List<POI> pois = poiService.findPOIsWithinDistance(center, distanceInMeters);
        return ResponseEntity.ok(pois);
    }

    // And this one for bounding box search
    @GetMapping("/search/bounds")
    public ResponseEntity<List<POI>> findPOIsWithinBoundingBox(
            @RequestParam("swLat") double swLat,
            @RequestParam("swLng") double swLng,
            @RequestParam("neLat") double neLat,
            @RequestParam("neLng") double neLng) {
        
        GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);
        Point southWest = gf.createPoint(new Coordinate(swLng, swLat));
        Point northEast = gf.createPoint(new Coordinate(neLng, neLat));
        
        List<POI> pois = poiService.findPOIsWithinBoundingBox(southWest, northEast);
        return ResponseEntity.ok(pois);
    }

}
