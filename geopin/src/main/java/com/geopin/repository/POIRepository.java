package com.geopin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.geopin.model.POI;
import com.geopin.model.POIStatus;

/**
 * Repository interface for POI (Point of Interest) entities.
 * 
 * In Spring Boot, repositories handle database operations. This interface extends
 * JpaRepository which gives us many useful methods out of the box. By extending
 * JpaRepository<POI, Long>, we're saying this repository works with POI entities
 * and uses Long as the type for ID values.
 * 
 * The @Repository annotation marks this as a Spring repository component.
 */
@Repository
public interface POIRepository extends JpaRepository<POI, Long> {
    /**
     * Since we're working with a small dataset (<200 POIs), we can rely on the
     * built-in JpaRepository methods:
     * 
     * - save(POI entity): saves or updates a POI
     * - findById(Long id): finds a POI by its ID
     * - findAll(): gets all POIs
     * - deleteById(Long id): deletes a POI by its ID
     * - delete(POI entity): deletes the given POI
     * - count(): counts total number of POIs
     * - existsById(Long id): checks if a POI exists
     * 
     * We can add simple finder methods here if needed, like:
     * - finding POIs by name
     * - finding POIs by status
     * - finding POIs by tag
     */

    // Find POIs by name (case-insensitive, partial match)
    List<POI> findByNameContainingIgnoreCase(String name);
    
    // Find POIs by status
    List<POI> findByStatus(POIStatus status);

    // Find POIs by tag name
    List<POI> findByTags_NameContainingIgnoreCase(String tagName);
}