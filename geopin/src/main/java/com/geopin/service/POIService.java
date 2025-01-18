package com.geopin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.geopin.exception.ResourceNotFoundException;
import com.geopin.model.POI;
import com.geopin.repository.POIRepository;
import org.locationtech.jts.geom.Point;

@Service  // This tells Spring this is a service class
public class POIService {
    
    private final POIRepository poiRepository;

    @Autowired
    public POIService(POIRepository poiRepository) {
        this.poiRepository = poiRepository;
    }

    // First, let's implement the save method we used in the controller
    public POI savePOI(POI poi) {
        // We can add validation or business logic here before saving
        return poiRepository.save(poi);
    }

    // Getting all POIs
    public List<POI> getAllPOIs() {
        return poiRepository.findAll();
    }

    // Getting a single POI by ID
    public POI getPOIById(Long id) {
        // We use orElseThrow because findById returns an Optional
        return poiRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("POI not found with id: " + id));
    }

    // Updating an existing POI
    public POI updatePOI(Long id, POI poiDetails) {
        // First find the existing POI
        POI poi = getPOIById(id);
        
        // Update the fields
        poi.setName(poiDetails.getName());
        poi.setDescription(poiDetails.getDescription());
        poi.setLatitude(poiDetails.getLatitude());
        poi.setLongitude(poiDetails.getLongitude());
        poi.setTags(poiDetails.getTags());
        
        // Save and return the updated POI
        return poiRepository.save(poi);
    }

    // Deleting a POI
    public void deletePOI(Long id) {
        // First check if it exists
        POI poi = getPOIById(id);
        poiRepository.delete(poi);
    }
    
    public List<POI> findPOIsWithinDistance(double latitude, double longitude, double distance) {
        return poiRepository.findPOIsWithinDistance(latitude, longitude, distance);
    }

    // And this one
    public List<POI> findPOIsWithinBoundingBox(Point southWest, Point northEast) {
        return poiRepository.findPOIsWithinBoundingBox(southWest, northEast);
    }
}