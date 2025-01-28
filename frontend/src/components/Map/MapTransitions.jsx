import { useEffect } from 'react';

const MapTransitions = ({ map, selectedPOI }) => {
  useEffect(() => {
    if (map && selectedPOI && window.google) {
      const position = {
        lat: selectedPOI.latitude,
        lng: selectedPOI.longitude
      };

      const currentCenter = map.getCenter().toJSON();
      const currentBounds = map.getBounds();
      
      // Check if target is within current viewport
      const isVisible = currentBounds && currentBounds.contains(
        new window.google.maps.LatLng(position)
      );
      
      if (isVisible) {
        // For visible locations, do smooth transition
        map.panTo(position);
        setTimeout(() => map.setZoom(15), 300);
      } else {
        // For distant locations:
        // 1. Zoom out to show more area
        map.setZoom(11);
        
        // 2. Move to an intermediate point
        const intermediatePosition = {
          lat: (currentCenter.lat + position.lat) / 2,
          lng: (currentCenter.lng + position.lng) / 2
        };
        
        // 3. Execute the transition sequence
        setTimeout(() => {
          map.panTo(intermediatePosition);
          setTimeout(() => {
            map.panTo(position);
            setTimeout(() => map.setZoom(15), 400);
          }, 400);
        }, 400);
      }
    }
  }, [map, selectedPOI]);
};

export default MapTransitions;  