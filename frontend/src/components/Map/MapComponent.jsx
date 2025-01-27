// SECTION 1: Imports at the top
import React, { useCallback, useState, useEffect } from 'react';
import { GoogleMap, useJsApiLoader } from '@react-google-maps/api';
import { DEFAULT_CENTER, DEFAULT_ZOOM } from '../../services/mapService';
import { poiService, tagService } from '../../services/api';
import MapControls from './MapControls';
import PinMarker from '../Pins/PinMarker';
import PinDetailModal from '../Pins/PinDetailModal';
import LoadingSpinner from '../shared/LoadingSpinner';

const libraries = ["places", "geometry"];
// SECTION 2: Component Function
const MapComponent = () => {
  // SECTION 2A: State declarations
  const [map, setMap] = useState(null);
  const [markers, setMarkers] = useState([]);
  const [selectedMarker, setSelectedMarker] = useState(null);
  const [loading, setLoading] = useState(true);

  // SECTION 2B: useJsApiLoader and other hooks
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

  // SECTION 2C: Event handlers and other functions
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
      console.log('Map instance when saving:', map); // Debug log
      
      if (!map) {
        console.log('No map instance available!');
        return;
      }
  
      // Store current map center and zoom before any state updates
      const currentCenter = map.getCenter().toJSON();
      const currentZoom = map.getZoom();
      
      console.log('Stored map position:', { currentCenter, currentZoom }); // Debug log
  
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
  
      // Add slight delay before restoring map view
      setTimeout(() => {
        console.log('Attempting to restore map view. Map instance:', map); // Debug log
        if (map) {
          map.setCenter(currentCenter);
          map.setZoom(currentZoom);
          console.log('Map view restored'); // Debug log
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

  //test backend connection
  const testBackendConnection = async () => {
    console.log('Starting API connection test...');
    
    try {
      // Test 1: Create a POI
      console.log('Test 1: Creating a new POI...');
      const newPOI = {
        name: "Test Location",
        latitude: 51.5074,  // London coordinates
        longitude: -0.1278,
        description: "Test description",
        status: "ACTIVE"
      };
      
      const createdPOI = await poiService.createPOI(newPOI);
      console.log('Created POI:', createdPOI);
      
      // Test 2: Get all POIs
      console.log('Test 2: Fetching all POIs...');
      const allPOIs = await poiService.getAllPOIs();
      console.log('All POIs:', allPOIs);
      
      // Test 3: Create a tag
      console.log('Test 3: Creating a new tag...');
      const newTag = { name: `test-tag-${Date.now()}` };
      const createdTag = await tagService.createTag(newTag);
      console.log('Created tag:', createdTag);
      
      // Test 4: Get all tags
      console.log('Test 4: Fetching all tags...');
      const allTags = await tagService.getAllTags();
      console.log('All tags:', allTags);
      
      // Test 5: Update the POI
      if (createdPOI?.id) {
        console.log('Test 5: Updating POI...');
        const updateData = {
          ...newPOI,
          name: "Updated Test Location",
          description: "Updated description"
        };
        const updatedPOI = await poiService.updatePOI(createdPOI.id, updateData);
        console.log('Updated POI:', updatedPOI);
      }
      
      // Test 6: Delete the POI
      if (createdPOI?.id) {
        console.log('Test 6: Deleting POI...');
        await poiService.deletePOI(createdPOI.id);
        console.log('POI deleted successfully');
      }
      
      console.log('All tests completed successfully!');
    } catch (error) {
      console.error('Test failed:', error);
    }
  };

  if (loadError) return <div>Map cannot be loaded right now, sorry.</div>;
  if (!isLoaded || loading) return <LoadingSpinner />;
  


  // SECTION 2D: Return statement with JSX
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
            onSave={handleMarkerSave}
            onDelete={handleMarkerDelete}
          />
        )}
      </GoogleMap>
      <button 
            onClick={testBackendConnection}
            style={{
                position: 'fixed',
                bottom: '20px',
                right: '20px',
                padding: '10px 20px',
                backgroundColor: '#4CAF50',
                color: 'white',
                border: 'none',
                borderRadius: '4px',
                cursor: 'pointer',
                zIndex: 1000
            }}
        >
            Test Backend Connection
        </button>
    </div>
  );
};

export default MapComponent;