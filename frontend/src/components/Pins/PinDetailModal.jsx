// PinDetailModal.jsx
import React, { useState } from 'react';
import Modal from '../shared/Modal';

const PinDetailModal = ({ marker, onClose, onSave, onDelete }) => {
  const [formData, setFormData] = useState({
    name: marker.name || '',
    notes: marker.notes || '',
    tags: marker.tags || []
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    onSave({
      ...marker,
      ...formData
    });
  };

  return (
    <Modal onClose={onClose}>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          value={formData.name}
          onChange={e => setFormData(prev => ({ ...prev, name: e.target.value }))}
          placeholder="Location name"
        />
        <textarea
          value={formData.notes}
          onChange={e => setFormData(prev => ({ ...prev, notes: e.target.value }))}
          placeholder="Notes"
        />
        <div className="button-group">
          <button type="submit">Save</button>
          <button type="button" onClick={() => onDelete(marker.id)}>Delete</button>
        </div>
      </form>
    </Modal>
  );
};

export default PinDetailModal;