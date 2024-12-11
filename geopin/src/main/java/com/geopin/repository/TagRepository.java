package com.geopin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.geopin.model.POI;
import com.geopin.model.Tag;

/**
 * Repository interface for Tag entities.
 * This interface provides all basic CRUD operations by extending JpaRepository.
 * 
 * JpaRepository provides the following methods out of the box:
 * - save(Tag entity): saves a Tag entity and returns the saved entity
 * - findById(Long id): finds a Tag by its ID and returns an Optional<Tag>
 * - findAll(): returns all Tags as a List<Tag>
 * - deleteById(Long id): deletes a Tag by its ID
 * - delete(Tag entity): deletes the given Tag entity
 * - count(): returns the total number of Tags
 * - existsById(Long id): checks if a Tag exists with the given ID
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    // Basic CRUD operations are inherited from JpaRepository
    // We'll add custom query methods below
	
	/**
     * Finds all POIs that have been tagged with a specific tag name.
     * Uses the bidirectional relationship between POI and Tag entities.
     * The query joins the POI and Tag tables through the join table.
     * 
     * @param tagName The name of the tag to search for
     * @return List of POIs that have the specified tag
     */
    @Query("SELECT DISTINCT p FROM POI p JOIN p.tags t WHERE t.name = :tagName")
    List<POI> findPOIsByTagName(@Param("tagName") String tagName);
}