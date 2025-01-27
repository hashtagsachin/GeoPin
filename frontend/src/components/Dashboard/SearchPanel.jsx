import React, { useState } from 'react';
import { Search } from 'lucide-react';

const SearchPanel = ({ pois, selectedTags, onTagToggle }) => {
  const [searchTerm, setSearchTerm] = useState('');
  
  // Get unique tags from all POIs
  const allTags = [...new Set(pois.flatMap(poi => 
    poi.tags.map(tag => tag.name)
  ))];

  return (
    <div className="space-y-4">
      {/* Search Input */}
      <div className="relative">
        <input
          type="text"
          placeholder="Search places..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="w-full p-2 pr-8 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
        <Search size={18} className="absolute right-2 top-2.5 text-gray-400" />
      </div>

      {/* Tags Filter */}
      <div className="space-y-2">
        <h3 className="font-medium text-gray-700">Filter by tags:</h3>
        <div className="flex flex-wrap gap-2">
          {allTags.map(tag => (
            <button
              key={tag}
              onClick={() => onTagToggle(tag)}
              className={`px-3 py-1 rounded-full text-sm transition-colors ${
                selectedTags.includes(tag)
                  ? 'bg-blue-100 text-blue-600'
                  : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
              }`}
            >
              {tag}
            </button>
          ))}
        </div>
      </div>

      {/* Search Results */}
      <div className="space-y-2">
        {pois
          .filter(poi => 
            poi.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
            poi.description?.toLowerCase().includes(searchTerm.toLowerCase())
          )
          .map(poi => (
            <div key={poi.id} className="p-3 border rounded hover:bg-gray-50">
              <h3 className="font-medium">{poi.name}</h3>
              {poi.description && (
                <p className="text-sm text-gray-600">{poi.description}</p>
              )}
              <div className="mt-2 flex flex-wrap gap-1">
                {poi.tags.map(tag => (
                  <span
                    key={tag.name}
                    className="px-2 py-0.5 bg-gray-100 text-gray-600 rounded-full text-xs"
                  >
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