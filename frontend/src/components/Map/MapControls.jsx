// MapControls.jsx
import React from 'react';

const MapControls = ({ map, onMarkerAdd }) => {
  const [longPressTimer, setLongPressTimer] = React.useState(null);

  const handleMouseDown = React.useCallback((e) => {
    const timer = setTimeout(() => {
      const lat = e.latLng.lat();
      const lng = e.latLng.lng();
      onMarkerAdd({ lat, lng });
    }, 500);
    setLongPressTimer(timer);
  }, [onMarkerAdd]);

  React.useEffect(() => {
    if (!map || !window.google) return;
    
    map.addListener('mousedown', handleMouseDown);
    map.addListener('mouseup', () => {
      if (longPressTimer) {
        clearTimeout(longPressTimer);
        setLongPressTimer(null);
      }
    });

    return () => {
      if (map && window.google) {
        window.google.maps.event.clearListeners(map, 'mousedown');
        window.google.maps.event.clearListeners(map, 'mouseup');
      }
    };
  }, [map, longPressTimer, handleMouseDown]);

  return null;
};

export default MapControls;