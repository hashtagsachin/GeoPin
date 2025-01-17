package com.geopin.model;

import jakarta.persistence.*;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a Point of Interest (POI) in the GeoPin application.
 * This entity stores location information along with metadata about places users want to remember.
 */
@Entity
@Table(name = "pois")
public class POI {
    // Static GeometryFactory for creating Point objects
    private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    /**
     * The geographical location stored as a PostGIS Point.
     * The SRID 4326 corresponds to the WGS84 coordinate system used by GPS.
     */
    @Column(name = "location")
    @JdbcTypeCode(SqlTypes.GEOMETRY)
    private Point location;

    /**
     * Latitude and longitude are stored separately for easier access and validation.
     * They are synchronized with the location Point through PrePersist and PreUpdate hooks.
     */
    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(name = "source_reference")
    private String sourceReference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private POIStatus status = POIStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "poi_tags",
        joinColumns = @JoinColumn(name = "poi_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    // Default constructor
    public POI() {}

    /**
     * Creates a new POI with required fields.
     * The location Point will be automatically created from latitude and longitude.
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

    // Existing getters and setters remain the same...
   

    /**
     * Adds a tag to this POI and maintains the bidirectional relationship.
     *
     * @param tag The tag to add
     */
    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.getPois().add(this);
    }

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

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
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

	/**
     * Removes a tag from this POI and maintains the bidirectional relationship.
     *
     * @param tag The tag to remove
     */
    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getPois().remove(this);
    }

    /**
     * Creates or updates the PostGIS Point geometry from latitude and longitude coordinates.
     * This method is automatically called before persisting or updating the entity.
     * 
     * The coordinates are stored in WGS84 (SRID: 4326) format, which is the standard
     * coordinate system used by GPS and most mapping applications.
     */
    @PrePersist
    @PreUpdate
    private void prepareLocation() {
        if (this.latitude != null && this.longitude != null) {
            // Create a new Point using the JTS GeometryFactory
            // Note: JTS uses (x,y) order, where x is longitude and y is latitude
            this.location = geometryFactory.createPoint(
                new Coordinate(this.longitude, this.latitude)
            );
        }
    }

    /**
     * Updates latitude and longitude from the Point geometry.
     * This method can be called after loading a POI from the database
     * to ensure the separate coordinate fields are in sync with the geometry.
     */
    @PostLoad
    private void loadCoordinates() {
        if (this.location != null) {
            this.longitude = this.location.getX();
            this.latitude = this.location.getY();
        }
    }
}