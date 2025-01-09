// MapComponent.jsx
import React, { useCallback, useState } from 'react';
import { 
  GoogleMap, 
  useJsApiLoader,
  Marker,
  InfoWindow 
} from '@react-google-maps/api';
import { 
  DEFAULT_CENTER, 
  DEFAULT_ZOOM, 
  defaultMapOptions, 
  required_libraries,
  MAP_ERROR_MESSAGES 
} from '../../services/mapService';

const MapComponent = () => {
  // Load the Google Maps JavaScript API and get the google object
  const { isLoaded, loadError } = useJsApiLoader({
    googleMapsApiKey: process.env.REACT_APP_GOOGLE_MAPS_API_KEY,
    libraries: required_libraries,
  });

  // Reference to maintain map instance
  const mapRef = React.useRef(null);
  // Reference to maintain Places service instance
  const placesService = React.useRef(null);

  // State for managing markers and selected places
  const [customMarker, setCustomMarker] = useState(null);
  const [selectedPlace, setSelectedPlace] = useState(null);
  const [longPressTimer, setLongPressTimer] = useState(null);
  const [isCustomMarkerInfoOpen, setIsCustomMarkerInfoOpen] = useState(false);


  // Handle mouse down for long press
  const handleMouseDown = useCallback((e) => {
    // Start a timer when mouse is pressed
    const timer = setTimeout(() => {
      // After 500ms, create a marker at the clicked location
      const lat = e.latLng.lat();
      const lng = e.latLng.lng();
      setCustomMarker({ position: { lat, lng } });
    }, 500); // 500ms for long press

    setLongPressTimer(timer);
  }, []);

  // Clear timer if mouse is released before long press
  const handleMouseUp = useCallback(() => {
    if (longPressTimer) {
      clearTimeout(longPressTimer);
      setLongPressTimer(null);
    }
  }, [longPressTimer]);

  //remove the marker
//handle clicking on marker
const handleCustomMarkerClick = useCallback(() => {
    //when marker is clicked show its info window
    setIsCustomMarkerInfoOpen(true);
  }, []);
//delete the marker
const handleDeleteMarker = useCallback (() => {
    //close InfoWindow
    setIsCustomMarkerInfoOpen(false);
    //remove the marker
    setCustomMarker(null);
}, []);

  // Handle place selection
  const handlePlaceClick = useCallback((place) => {
    setSelectedPlace(place);
  }, []);

  // Handle saving a place as POI
  const handleSavePOI = useCallback(() => {
    if (selectedPlace) {
      // Here we'll add the logic to save the POI
      console.log('Saving POI:', selectedPlace);
      // You would typically open a form here to add tags
      // and any additional information
    }
  }, [selectedPlace]);

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
        options={{
          ...defaultMapOptions,
          clickableIcons: true,
        }}
        onMouseDown={handleMouseDown}
        onMouseUp={handleMouseUp}
        onLoad={map => {
            mapRef.current = map;
            
            // Initialize Places service with the map instance
            if (window.google) {  // Make sure google object exists
              placesService.current = new window.google.maps.places.PlacesService(map);
              
              // Add click listener for place selection
              map.addListener('click', (e) => {
                // Log the click event to debug
                console.log('Map clicked:', e);
                
                if (e.placeId) {
                  console.log('Place clicked:', e.placeId);
                  
                  placesService.current.getDetails({
                    placeId: e.placeId,
                    fields: ['name', 'geometry', 'formatted_address']
                  }, (place, status) => {
                    console.log('Place details:', status, place);
                    if (status === 'OK') {
                      handlePlaceClick(place);
                    }
                  });
                }
              });
            }
          }}
      >
        {/* Show custom marker from long press */}
        {customMarker && (
          <>
            <Marker
              position={customMarker.position}
              draggable={true}
              onClick={handleCustomMarkerClick}
            />
            {isCustomMarkerInfoOpen && (
              <InfoWindow
                position={customMarker.position}
                onCloseClick={() => setIsCustomMarkerInfoOpen(false)}
              >
                <div>
                  <h3>Custom Marker</h3>
                  <button 
                    onClick={handleDeleteMarker}
                    style={{
                      padding: '8px 16px',
                      backgroundColor: '#dc3545',
                      color: 'white',
                      border: 'none',
                      borderRadius: '4px',
                      cursor: 'pointer',
                      marginTop: '8px'
                    }}
                  >
                    Delete Marker
                  </button>
                </div>
              </InfoWindow>
            )}
          </>
        )}

        {/* Show info window for selected place */}
        {selectedPlace && (
          <InfoWindow
            position={selectedPlace.geometry.location}
            onCloseClick={() => setSelectedPlace(null)}
          >
            <div>
              <h3>{selectedPlace.name}</h3>
              <p>{selectedPlace.formatted_address}</p>
              <button 
                onClick={handleSavePOI}
                style={{
                  padding: '8px 16px',
                  backgroundColor: '#4285f4',
                  color: 'white',
                  border: 'none',
                  borderRadius: '4px',
                  cursor: 'pointer'
                }}
              >
                Save as POI
              </button>
            </div>
          </InfoWindow>
        )}
      </GoogleMap>
    </div>
  );
};

export default MapComponent;