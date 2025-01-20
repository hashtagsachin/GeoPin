package com.geopin.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * POI (Point of Interest) Entity Class
 * 
 * This class represents the core data model for storing locations in the application.
 * In Spring Boot, an Entity class directly maps to a database table. Each instance
 * of this class will become a row in the database.
 *
 * The @Entity annotation tells Spring Boot this is a JPA entity class.
 * The @Table annotation specifies the database table name (in this case, "pois").
 */
@Entity
@Table(name = "pois")
public class POI {
    
    /**
     * The unique identifier for each POI.
     * 
     * @Id marks this as the primary key in the database
     * @GeneratedValue tells Spring to automatically generate new values for each POI
     * IDENTITY strategy means the database will handle ID generation
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the Point of Interest.
     * @Column(nullable = false) ensures this field cannot be empty in the database
     */
    @Column(nullable = false)
    private String name;

    /**
     * A detailed description of the POI.
     * columnDefinition = "text" allows for longer text content than standard VARCHAR
     */
    @Column(columnDefinition = "text")
    private String description;

    /**
     * The geographical coordinates of the POI.
     * These are stored as decimal degrees using the WGS84 coordinate system
     * (the same system used by GPS and Google Maps).
     * 
     * Valid latitude ranges from -90 to +90 degrees
     * Valid longitude ranges from -180 to +180 degrees
     */
    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    /**
     * Optional reference to the source of this POI data
     * (e.g., "Google Maps", "Personal Visit", etc.)
     */
    @Column(name = "source_reference")
    private String sourceReference;

    /**
     * The current status of the POI.
     * @Enumerated tells JPA to store the enum as a STRING in the database
     * The default value is set to ACTIVE when creating new POIs
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private POIStatus status = POIStatus.ACTIVE;

    /**
     * Automatically managed timestamps for entity creation and updates.
     * @CreationTimestamp and @UpdateTimestamp are Hibernate annotations that
     * automatically handle these values for us.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * The tags associated with this POI.
     * 
     * @ManyToMany indicates a many-to-many relationship with the Tag entity
     * FetchType.LAZY means tags will only be loaded from the database when explicitly accessed
     * @JoinTable defines the intermediate table that manages the many-to-many relationship
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "poi_tags",
        joinColumns = @JoinColumn(name = "poi_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    /**
     * Default constructor required by JPA.
     * Spring Boot needs this to create POI instances when loading from the database.
     */
    public POI() {}

    /**
     * Main constructor for creating new POIs with the required fields.
     * 
     * @param name      The name of the POI
     * @param latitude  The latitude coordinate
     * @param longitude The longitude coordinate
     */
    public POI(String name, Double latitude, Double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Returns the location as a Map for easy JSON serialization.
     * This is particularly useful for REST API responses where we want
     * to represent the location in a standard format.
     * 
     * @return Map containing latitude and longitude
     */
    public Map<String, Double> getLocation() {
        Map<String, Double> locationMap = new HashMap<>();
        locationMap.put("latitude", this.latitude);
        locationMap.put("longitude", this.longitude);
        return locationMap;
    }

    /**
     * Adds a tag to this POI and maintains the bidirectional relationship.
     * In a bidirectional relationship, we need to update both sides
     * to keep the object model consistent.
     * 
     * @param tag The tag to add to this POI
     */
    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.getPois().add(this);
    }

    /**
     * Removes a tag from this POI and maintains the bidirectional relationship.
     * 
     * @param tag The tag to remove from this POI
     */
    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getPois().remove(this);
    }

    // Standard getters and setters
    // In Spring Boot, these are used by the framework for data binding
    // and JSON serialization/deserialization

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getSourceReference() {
        return sourceReference;
    }

    public void setSourceReference(String sourceReference) {
        this.sourceReference = sourceReference;
    }

    public POIStatus getStatus() {
        return status;
    }

    public void setStatus(POIStatus status) {
        this.status = status;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}