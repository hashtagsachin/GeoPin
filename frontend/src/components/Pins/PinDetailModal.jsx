// PinDetailModal.jsx
import React, { useState } from 'react';
import Modal from '../shared/Modal';
import { X, Tag, MapPin } from 'lucide-react';
import './PinDetailModal.css';  // Import our CSS file

const PinDetailModal = ({ marker, onClose, onSave, onDelete }) => {
  const [formData, setFormData] = useState({
    name: marker.name || '',
    notes: marker.notes || '',
    tags: marker.tags || []
  });
  const [newTag, setNewTag] = useState('');

  // Add a new function to handle adding tags
  const handleAddTag = () => {
    if (newTag.trim() && !formData.tags.includes(newTag.trim())) {
      setFormData(prev => ({
        ...prev,
        tags: [...prev.tags, newTag.trim()]
      }));
      setNewTag('');
    }
  };

  // Add a function to remove tags
  const handleRemoveTag = (tagToRemove) => {
    setFormData(prev => ({
      ...prev,
      tags: prev.tags.filter(tag => tag !== tagToRemove)
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSave({
      ...marker,
      ...formData
    });
  };

  return (
    <Modal onClose={onClose}>
      <div className="modal-content">
        {/* Header */}
        <div className="modal-header">
          <div className="header-title">
            <MapPin size={20} />
            <h3>Location Details</h3>
          </div>
          <button className="close-button" onClick={onClose}>
            <X size={20} />
          </button>
        </div>

        <form onSubmit={handleSubmit}>
          {/* Name Input */}
          <div className="form-group">
            <label className="form-label">
              Location Name
            </label>
            <input
              type="text"
              value={formData.name}
              onChange={e => setFormData(prev => ({ ...prev, name: e.target.value }))}
              className="form-input"
              placeholder="Enter location name"
            />
          </div>

          {/* Tags Section */}
          <div className="form-group">
            <label className="form-label">
              Tags
            </label>
            <div className="tags-container">
              {formData.tags.map((tag) => (
                <span key={tag} className="tag">
                  <Tag className="tag-icon" size={16} />
                  {tag}
                  <button onClick={() => handleRemoveTag(tag)}>
                    <X size={16} />
                  </button>
                </span>
              ))}
            </div>
            <div className="tag-input-container">
              <input
                type="text"
                value={newTag}
                onChange={(e) => setNewTag(e.target.value)}
                className="form-input"
                placeholder="Add a tag"
                onKeyPress={(e) => e.key === 'Enter' && handleAddTag()}
              />
              <button
                type="button"
                onClick={handleAddTag}
                className="add-tag-button"
              >
                Add
              </button>
            </div>
          </div>

          {/* Notes Section */}
          <div className="form-group">
            <label className="form-label">
              Notes
            </label>
            <textarea
              value={formData.notes}
              onChange={e => setFormData(prev => ({ ...prev, notes: e.target.value }))}
              className="form-input"
              rows="3"
              placeholder="Add notes about this location..."
            />
          </div>

          {/* Action Buttons */}
          <div className="action-buttons">
            <button
              type="button"
              onClick={() => onDelete(marker.id)}
              className="delete-button"
            >
              Delete
            </button>
            <button
              type="submit"
              className="save-button"
            >
              Save
            </button>
          </div>
        </form>
      </div>
    </Modal>
  );
};

export default PinDetailModal;