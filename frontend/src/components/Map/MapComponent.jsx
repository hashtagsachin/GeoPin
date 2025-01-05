// MapComponent.jsx
import React, { useCallback } from 'react';
import { GoogleMap, useJsApiLoader } from '@react-google-maps/api';
import { 
  DEFAULT_CENTER, 
  DEFAULT_ZOOM, 
  defaultMapOptions, 
  required_libraries,
  MAP_ERROR_MESSAGES 
} from '../../services/mapService';

const MapComponent = () => {
  // Load the Google Maps JavaScript API
  const { isLoaded, loadError } = useJsApiLoader({
    googleMapsApiKey: process.env.REACT_APP_GOOGLE_MAPS_API_KEY,
    libraries: required_libraries,
  });

  // Reference to maintain map instance
  const mapRef = React.useRef(null);

  // Callback when map instance is created
  const onLoad = useCallback((map) => {
    mapRef.current = map;
    // You can add additional initialization logic here
    console.log("Map component loaded successfully");
  }, []);

  // Callback when map instance is unmounted
  const onUnmount = useCallback(() => {
    mapRef.current = null;
  }, []);

  // Handle loading error
  if (loadError) {
    return <div className="map-error">{MAP_ERROR_MESSAGES.LOAD_FAILED}</div>;
  }

  // Show loading state
  if (!isLoaded) {
    return <div className="map-loading">Loading map...</div>;
  }

  return (
    <div style={{ height: '100vh', width: '100%' }}>
      <GoogleMap
        mapContainerStyle={{ height: '100%', width: '100%' }}
        center={DEFAULT_CENTER}
        zoom={DEFAULT_ZOOM}
        options={defaultMapOptions}
        onLoad={onLoad}
        onUnmount={onUnmount}
      >
        {/* Child components like markers will go here */}
      </GoogleMap>
    </div>
  );
};

export default MapComponent;