// mapService.js
// This service centralizes our Google Maps configuration and provides
// reusable map settings across our application

// Default map settings for London center coordinates
export const DEFAULT_CENTER = {
    lat: 51.5074,
    lng: -0.1278
  };
  
  // Default zoom level suitable for city navigation
  export const DEFAULT_ZOOM = 13;
  
  // Map configuration options that will be reused across components
  export const defaultMapOptions = {
    // Disable default UI elements we'll implement ourselves
    disableDefaultUI: true,
    // Enable zoom control for better user experience
    zoomControl: true,
    // Restrict maximum zoom to maintain performance
    maxZoom: 18,
    // Restrict minimum zoom to keep context
    minZoom: 8,
    // Set map style to roadmap for clearest POI visibility
    mapTypeId: "roadmap",
    // Enable scroll wheel zooming for natural interaction
    scrollwheel: true,
    // Disable 45-degree imagery as we're focusing on 2D navigation
    tilt: 0
  };
  
  // Libraries we need to load with the Maps API
  export const required_libraries = ["places", "geometry"];
  
  // Error message templates for common map-related issues
  export const MAP_ERROR_MESSAGES = {
    LOAD_FAILED: "Failed to load Google Maps. Please refresh the page.",
    GEOLOCATION_DENIED: "Location access was denied. Defaulting to London center.",
    GENERAL_ERROR: "An error occurred with the map. Please try again."
  };