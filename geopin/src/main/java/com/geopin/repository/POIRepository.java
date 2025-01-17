package com.geopin.repository;


import java.util.List;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.geopin.model.POI;

/**
 * Repository interface for POI (Point of Interest) entities.
 * This interface provides all basic CRUD operations by extending JpaRepository.
 * 
 * JpaRepository provides the following methods out of the box:
 * - save(POI entity): saves a POI entity and returns the saved entity
 * - findById(Long id): finds a POI by its ID and returns an Optional<POI>
 * - findAll(): returns all POIs as a List<POI>
 * - deleteById(Long id): deletes a POI by its ID
 * - delete(POI entity): deletes the given POI entity
 * - count(): returns the total number of POIs
 * - existsById(Long id): checks if a POI exists with the given ID
 */
@Repository
public interface POIRepository extends JpaRepository<POI, Long> {
    // Basic CRUD operations are inherited from JpaRepository
    // We'll add custom query methods below
	
	
	/**
     * Finds all POIs within a specified distance from a given point.
     * Uses PostGIS ST_DWithin for efficient radius search.
     * ST_DWithin is more efficient than ST_Distance as it uses spatial indexes.
     * 
     * @param location The center point to search from
     * @param distanceInMeters The search radius in meters
     * @return List of POIs within the specified distance
     */
    @Query(value = "SELECT p.* FROM poi p WHERE ST_DWithin(" +
           "p.location, :location, :distanceInMeters, true)", 
           nativeQuery = true)
    List<POI> findPOIsWithinDistance(
        @Param("location") Point location, 
        @Param("distanceInMeters") double distanceInMeters
    );

    /**
     * Finds all POIs within a rectangular bounding box defined by two corner points.
     * Uses PostGIS ST_MakeEnvelope to create a bounding box and ST_Within for the spatial query.
     * 
     * @param southWest The bottom-left corner of the bounding box
     * @param northEast The top-right corner of the bounding box
     * @return List of POIs within the bounding boxx ok
     */
    @Query(value = "SELECT p.* FROM poi p WHERE ST_Within(" +
           "p.location, ST_MakeEnvelope(" +
           "ST_X(:southWest), ST_Y(:southWest), " +
           "ST_X(:northEast), ST_Y(:northEast), 4326))",
           nativeQuery = true)
    List<POI> findPOIsWithinBoundingBox(
        @Param("southWest") Point southWest,
        @Param("northEast") Point northEast
    );
	
}