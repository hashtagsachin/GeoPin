import React, { useState } from 'react';
import { Search } from 'lucide-react';

const SearchPanel = ({ pois, selectedTags, onTagToggle, onPOISelect }) => {
  const [searchTerm, setSearchTerm] = useState('');
  
  const allTags = [...new Set(pois.flatMap(poi => 
    poi.tags.map(tag => tag.name)
  ))];

  const filteredPOIs = pois.filter(poi => 
    (poi.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    poi.description?.toLowerCase().includes(searchTerm.toLowerCase())) &&
    (selectedTags.length === 0 || poi.tags.some(tag => selectedTags.includes(tag.name)))
  );

  return (
    <div>
      <div className="search-container">
        <input
          type="text"
          placeholder="Search places..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="search-input"
        />
        <Search size={18} className="search-icon" />
      </div>

      <div className="tag-section">
        <h3>Filter by tags:</h3>
        <div className="tag-container">
          {allTags.map(tag => (
            <button
              key={tag}
              onClick={() => onTagToggle(tag)}
              className={`tag-button ${selectedTags.includes(tag) ? 'active' : ''}`}
            >
              {tag}
            </button>
          ))}
        </div>
      </div>

      <div className="search-results">
        {filteredPOIs.map(poi => (
          <div 
            key={poi.id} 
            className="poi-card"
            onClick={() => onPOISelect(poi)}
            style={{ cursor: 'pointer' }}
          >
            <h3>{poi.name}</h3>
            {poi.description && (
              <p>{poi.description}</p>
            )}
            <div className="poi-tags">
              {poi.tags.map(tag => (
                <span key={tag.name} className="poi-tag">
                  {tag.name}
                </span>
              ))}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default SearchPanel;