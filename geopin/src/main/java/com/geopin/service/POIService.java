package com.geopin.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.geopin.exception.ResourceNotFoundException;
import com.geopin.model.POI;
import com.geopin.repository.POIRepository;
import com.geopin.util.GeoCalculator;

@Service
public class POIService {
    
    private final POIRepository poiRepository;

    @Autowired
    public POIService(POIRepository poiRepository) {
        this.poiRepository = poiRepository;
    }

    /**
     * Creates a new POI in the database.
     * This method handles the creation of new Points of Interest.
     * 
     * @param poi The POI object to save
     * @return The saved POI with its generated ID
     */
    public POI savePOI(POI poi) {
        return poiRepository.save(poi);
    }

    /**
     * Retrieves all POIs from the database.
     * 
     * @return List of all POIs
     */
    public List<POI> getAllPOIs() {
        return poiRepository.findAll();
    }

    /**
     * Retrieves a specific POI by its ID.
     * If the POI is not found, throws a ResourceNotFoundException.
     * 
     * @param id The ID of the POI to retrieve
     * @return The found POI
     * @throws ResourceNotFoundException if POI is not found
     */
    public POI getPOIById(Long id) {
        return poiRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("POI not found with id: " + id));
    }

    /**
     * Updates an existing POI.
     * This method first checks if the POI exists, then updates its fields.
     * 
     * @param id The ID of the POI to update
     * @param poiDetails The new POI details
     * @return The updated POI
     * @throws ResourceNotFoundException if POI is not found
     */
    public POI updatePOI(Long id, POI poiDetails) {
        POI poi = getPOIById(id);
        
        // Update the fields
        poi.setName(poiDetails.getName());
        poi.setDescription(poiDetails.getDescription());
        poi.setLatitude(poiDetails.getLatitude());
        poi.setLongitude(poiDetails.getLongitude());
        if (poiDetails.getTags() != null) {
            poi.setTags(poiDetails.getTags());
        }
        
        return poiRepository.save(poi);
    }

    /**
     * Deletes a POI by its ID.
     * Throws an exception if the POI doesn't exist.
     * 
     * @param id The ID of the POI to delete
     * @throws ResourceNotFoundException if POI is not found
     */
    public void deletePOI(Long id) {
        POI poi = getPOIById(id);
        poiRepository.delete(poi);
    }

    
    /**
     * Finds all POIs within a specified distance from a central point.
     * This method uses the haversine formula to calculate real-world distances.
     * 
     * @param latitude Center point latitude
     * @param longitude Center point longitude
     * @param distanceInMeters Search radius in meters
     * @return List of POIs within the specified distance
     */
    public List<POI> findPOIsWithinDistance(double latitude, double longitude, double distanceInMeters) {
        // Get all POIs and filter them using our distance calculation
        return poiRepository.findAll().stream()
            .filter(poi -> {
                double distance = GeoCalculator.calculateDistance(
                    latitude, longitude,
                    poi.getLatitude(), poi.getLongitude()
                );
                return distance <= distanceInMeters;
            })
            .collect(Collectors.toList());
    }

    /**
     * Finds all POIs within a rectangular bounding box area.
     * The box is defined by its southwest and northeast corners.
     * 
     * @param swLat Southwest corner latitude
     * @param swLon Southwest corner longitude
     * @param neLat Northeast corner latitude
     * @param neLon Northeast corner longitude
     * @return List of POIs within the bounding box
     */
    public List<POI> findPOIsWithinBoundingBox(double swLat, double swLon, 
                                              double neLat, double neLon) {
        return poiRepository.findAll().stream()
            .filter(poi -> GeoCalculator.isWithinBoundingBox(
                poi.getLatitude(), poi.getLongitude(),
                swLat, swLon, neLat, neLon
            ))
            .collect(Collectors.toList());
    }
}