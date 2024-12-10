package com.geopin.model;

/**
 * Represents the various states a Point of Interest (POI) can be in.
 * This enum helps manage the lifecycle and visibility of POIs in the system.
 */
public enum POIStatus {
    /**
     * Normal, visible POI that is currently relevant
     */
    ACTIVE,
    
    /**
     * POI that has been hidden but preserved for historical purposes
     */
    ARCHIVED,
    
    /**
     * POI representing a temporary location or time-limited event
     */
    TEMPORARY
}