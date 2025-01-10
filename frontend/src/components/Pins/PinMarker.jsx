// PinMarker.jsx - Individual marker management
import React from 'react';
import { Marker } from '@react-google-maps/api';

const PinMarker = ({ marker, onSelect }) => {
  return (
    <Marker
      position={marker.position}
      draggable={true}
      onClick={() => onSelect(marker)}
    />
  );
};

export default PinMarker;  // Added export