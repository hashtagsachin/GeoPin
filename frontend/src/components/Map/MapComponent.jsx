import React, { useCallback, useState, useEffect } from 'react';
import { GoogleMap, useJsApiLoader } from '@react-google-maps/api';
import { DEFAULT_CENTER, DEFAULT_ZOOM } from '../../services/mapService';
import { poiService } from '../../services/api';
import MapControls from './MapControls';
import PinMarker from '../Pins/PinMarker';
import PinDetailModal from '../Pins/PinDetailModal';
import LoadingSpinner from '../shared/LoadingSpinner';

import MapTransitions from './MapTransitions';

const libraries = ["places", "geometry"];

const MapComponent = ({ selectedPOI }) => {
  const [map, setMap] = useState(null);
  const [markers, setMarkers] = useState([]);
  const [selectedMarker, setSelectedMarker] = useState(null);
  const [loading, setLoading] = useState(true);

  const { isLoaded, loadError } = useJsApiLoader({
    googleMapsApiKey: process.env.REACT_APP_GOOGLE_MAPS_API_KEY,
    libraries
  });

  // Load existing POIs on mount
  useEffect(() => {
    const loadPOIs = async () => {
      try {
        const pois = await poiService.getAllPOIs();
        const loadedMarkers = pois.map(poi => ({
          id: poi.id,
          position: {
            lat: poi.latitude,
            lng: poi.longitude
          },
          name: poi.name,
          description: poi.description,
          tags: poi.tags.map(tag => tag.name),
          status: poi.status
        }));
        setMarkers(loadedMarkers);
      } catch (error) {
        console.error('Error loading POIs:', error);
      } finally {
        setLoading(false);
      }
    };

    if (isLoaded) {
      loadPOIs();
    }
  }, [isLoaded]);

 

  
  const handleMapLoad = useCallback((mapInstance) => {
    setMap(mapInstance);
  }, []);

  const handleMarkerAdd = useCallback((position) => {
    setMarkers(prev => [...prev, { 
      id: `temp_${Date.now()}`,
      position,
      status: 'ACTIVE'
    }]);
  }, []);

  const handleMarkerSave = async (updatedMarker) => {
    try {
      if (!map) return;

      // Store current map center and zoom
      const currentCenter = map.getCenter().toJSON();
      const currentZoom = map.getZoom();

      const poiData = {
        name: updatedMarker.name,
        latitude: updatedMarker.position.lat,
        longitude: updatedMarker.position.lng,
        description: updatedMarker.description,
        status: updatedMarker.status || 'ACTIVE'
      };

      let savedPOI;
      if (updatedMarker.id.toString().startsWith('temp_')) {
        savedPOI = await poiService.createPOI(poiData);
      } else {
        savedPOI = await poiService.updatePOI(updatedMarker.id, poiData);
      }

      setMarkers(prev => prev.map(m => 
        m.id === updatedMarker.id ? {
          id: savedPOI.id,
          position: {
            lat: savedPOI.latitude,
            lng: savedPOI.longitude
          },
          name: savedPOI.name,
          description: savedPOI.description,
          tags: savedPOI.tags.map(tag => tag.name),
          status: savedPOI.status
        } : m
      ));
      setSelectedMarker(null);

      // Restore map view with a slight delay
      setTimeout(() => {
        if (map) {
          map.setCenter(currentCenter);
          map.setZoom(currentZoom);
        }
      }, 100);
    } catch (error) {
      console.error('Error saving POI:', error);
    }
  };

  const handleMarkerDelete = async (markerId) => {
    try {
      if (!markerId.toString().startsWith('temp_')) {
        await poiService.deletePOI(markerId);
      }
      setMarkers(prev => prev.filter(m => m.id !== markerId));
      setSelectedMarker(null);
    } catch (error) {
      console.error('Error deleting POI:', error);
    }
  };

  if (loadError) return <div>Map cannot be loaded right now, sorry.</div>;
  if (!isLoaded || loading) return <LoadingSpinner />;

  return (
    <div style={{ height: '100vh', width: '100%' }}>
      <GoogleMap
        mapContainerStyle={{ height: '100%', width: '100%' }}
        center={DEFAULT_CENTER}
        zoom={DEFAULT_ZOOM}
        onLoad={handleMapLoad}
      >
        <MapTransitions map={map} selectedPOI={selectedPOI} />
          
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
            onSave={handleMarkerSave}
            onDelete={handleMarkerDelete}
          />
        )}
      </GoogleMap>
    </div>
  );
};

export default MapComponent;