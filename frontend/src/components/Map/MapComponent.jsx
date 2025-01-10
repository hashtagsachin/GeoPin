// MapComponent.jsx - The orchestrator
import React, { useCallback, useState } from 'react';
import { GoogleMap, useJsApiLoader } from '@react-google-maps/api';
import { DEFAULT_CENTER, DEFAULT_ZOOM } from '../../services/mapService';
import MapControls from './MapControls';
import PinMarker from '../Pins/PinMarker';
import PinDetailModal from '../Pins/PinDetailModal';
import LoadingSpinner from '../shared/LoadingSpinner';

const MapComponent = () => {
  // Core map state
  const [map, setMap] = useState(null);
  const [markers, setMarkers] = useState([]);
  const [selectedMarker, setSelectedMarker] = useState(null);
  
  const { isLoaded, loadError } = useJsApiLoader({
    googleMapsApiKey: process.env.REACT_APP_GOOGLE_MAPS_API_KEY,
    libraries: ["places", "geometry"],
  });

  const handleMapLoad = useCallback((mapInstance) => {
    setMap(mapInstance);
  }, []);

  const handleMarkerAdd = useCallback((position) => {
    setMarkers(prev => [...prev, { id: Date.now(), position }]);
  }, []);

  if (loadError) return <div>Map cannot be loaded right now, sorry.</div>;
  if (!isLoaded) return <LoadingSpinner />;

  return (
    <div style={{ height: '100vh', width: '100%' }}>
      <GoogleMap
        mapContainerStyle={{ height: '100%', width: '100%' }}
        center={DEFAULT_CENTER}
        zoom={DEFAULT_ZOOM}
        onLoad={handleMapLoad}
      >
        <MapControls 
        map={map}
        onMarkerAdd={handleMarkerAdd}
         />
        
        {markers.map(marker => (
          <PinMarker
            key={marker.id}
            marker={marker}
            onSelect={() => setSelectedMarker(marker)}
          />
        ))}

        {selectedMarker && (
          <PinDetailModal
            marker={selectedMarker}
            onClose={() => setSelectedMarker(null)}
            onSave={(updatedMarker) => {
              setMarkers(prev => 
                prev.map(m => m.id === updatedMarker.id ? updatedMarker : m)
              );
              setSelectedMarker(null);
            }}
            onDelete={(markerId) => {
              setMarkers(prev => prev.filter(m => m.id !== markerId));
              setSelectedMarker(null);
            }}
          />
        )}
      </GoogleMap>
    </div>
  );
};

export default MapComponent;