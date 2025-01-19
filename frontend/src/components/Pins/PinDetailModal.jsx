import React, { useState, useEffect } from 'react';
import Modal from '../shared/Modal';
import { X, Tag as TagIcon, MapPin, Loader } from 'lucide-react';
import { tagService } from '../../services/api';
import './PinDetailModal.css';

const PinDetailModal = ({ marker, onClose, onSave, onDelete }) => {
  // Main form state
  const [formData, setFormData] = useState({
    name: marker.name || '',
    description: marker.description || '',
    tags: marker.tags || []
  });
  
  // UI state
  const [newTag, setNewTag] = useState('');
  const [existingTags, setExistingTags] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [tagLoading, setTagLoading] = useState(true);

  // Load existing tags when modal opens
  useEffect(() => {
    const loadTags = async () => {
      try {
        const tags = await tagService.getAllTags();
        // Extract tag names from the tag objects
        setExistingTags(tags.map(tag => tag.name));
      } catch (error) {
        console.error('Error loading tags:', error);
      } finally {
        setTagLoading(false);
      }
    };
    loadTags();
  }, []);

  const handleAddTag = async () => {
    const tagName = newTag.trim();
    if (tagName && !formData.tags.includes(tagName)) {
      try {
        setTagLoading(true);
        
        // If it's a new tag, create it in the backend
        if (!existingTags.includes(tagName)) {
          await tagService.createTag({ name: tagName });
          setExistingTags(prev => [...prev, tagName]);
        }

        // Add to form data
        setFormData(prev => ({
          ...prev,
          tags: [...prev.tags, tagName]
        }));
        
        setNewTag('');
      } catch (error) {
        console.error('Error adding tag:', error);
      } finally {
        setTagLoading(false);
      }
    }
  };

  const handleRemoveTag = (tagToRemove) => {
    setFormData(prev => ({
      ...prev,
      tags: prev.tags.filter(tag => tag !== tagToRemove)
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    try {
      // Combine marker position data with form data
      const updatedMarker = {
        ...marker,
        ...formData
      };
      await onSave(updatedMarker);
    } catch (error) {
      console.error('Error saving location:', error);
    } finally {
      setIsLoading(false);
    }
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
          <button 
            className="close-button" 
            onClick={onClose}
            disabled={isLoading}
          >
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
              onChange={e => setFormData(prev => ({ 
                ...prev, 
                name: e.target.value 
              }))}
              className="form-input"
              placeholder="Enter location name"
              disabled={isLoading}
              required
            />
          </div>

          {/* Tags Section */}
          <div className="form-group">
            <label className="form-label">
              Tags
              {tagLoading && <Loader className="inline-loader" size={16} />}
            </label>
            <div className="tags-container">
              {formData.tags.map((tag) => (
                <span key={tag} className="tag">
                  <TagIcon className="tag-icon" size={16} />
                  {tag}
                  <button 
                    type="button"
                    onClick={() => handleRemoveTag(tag)}
                    disabled={isLoading}
                  >
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
                onKeyPress={(e) => {
                  if (e.key === 'Enter') {
                    e.preventDefault();
                    handleAddTag();
                  }
                }}
                list="existing-tags"
                disabled={isLoading || tagLoading}
              />
              <datalist id="existing-tags">
                {existingTags.map(tag => (
                  <option key={tag} value={tag} />
                ))}
              </datalist>
              <button
                type="button"
                onClick={handleAddTag}
                className="add-tag-button"
                disabled={isLoading || tagLoading}
              >
                Add
              </button>
            </div>
          </div>

          {/* Description Section */}
          <div className="form-group">
            <label className="form-label">
              Description
            </label>
            <textarea
              value={formData.description}
              onChange={e => setFormData(prev => ({ 
                ...prev, 
                description: e.target.value 
              }))}
              className="form-input"
              rows="3"
              placeholder="Add details about this location..."
              disabled={isLoading}
            />
          </div>

          {/* Action Buttons */}
          <div className="action-buttons">
            <button
              type="button"
              onClick={() => onDelete(marker.id)}
              className="delete-button"
              disabled={isLoading}
            >
              Delete
            </button>
            <button
              type="submit"
              className="save-button"
              disabled={isLoading}
            >
              {isLoading ? (
                <>
                  <Loader className="spinner" size={16} />
                  Saving...
                </>
              ) : 'Save'}
            </button>
          </div>
        </form>
      </div>
    </Modal>
  );
};

export default PinDetailModal;