package com.geopin.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // The mappedBy attribute indicates that the tags field in POI owns the relationship
    @ManyToMany(mappedBy = "tags")
    private Set<POI> pois = new HashSet<>();

    // Default constructor
    public Tag() {}

    // Constructor with name
    public Tag(String name) {
        this.name = name;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Set<POI> getPois() {
        return pois;
    }

    public void setPois(Set<POI> pois) {
        this.pois = pois;
    }

    // Utility methods to maintain the relationship
    public void addPOI(POI poi) {
        this.pois.add(poi);
        poi.getTags().add(this);
    }

    public void removePOI(POI poi) {
        this.pois.remove(poi);
        poi.getTags().remove(this);
    }
}