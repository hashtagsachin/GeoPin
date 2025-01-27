import React from 'react';
import { Search, MapPin, Tag } from 'lucide-react';
import SearchPanel from './SearchPanel';
import SavedPlacesPanel from './SavedPlacesPanel';
import TagsPanel from './TagsPanel';

const Sidebar = ({ 
  isOpen, 
  onToggle, 
  activeTab, 
  onTabChange, 
  pois, 
  selectedTags, 
  onTagToggle 
}) => {
  return (
    <div className={`bg-white border-r border-gray-200 ${isOpen ? 'w-80' : 'w-16'} flex flex-col transition-all`}>
      {/* Header */}
      <div className="p-4 border-b border-gray-200 flex justify-between items-center">
        {isOpen && <h1 className="text-xl font-semibold">GeoPin</h1>}
        <button onClick={onToggle} className="p-2 hover:bg-gray-100 rounded">
          <Menu size={20} />
        </button>
      </div>

      {/* Navigation */}
      <div className="flex flex-col p-2 space-y-2">
        <button 
          onClick={() => onTabChange('search')}
          className={`flex items-center p-2 rounded ${activeTab === 'search' ? 'bg-blue-100 text-blue-600' : 'hover:bg-gray-100'}`}
        >
          <Search size={20} />
          {isOpen && <span className="ml-3">Search POIs</span>}
        </button>
        <button 
          onClick={() => onTabChange('saved')}
          className={`flex items-center p-2 rounded ${activeTab === 'saved' ? 'bg-blue-100 text-blue-600' : 'hover:bg-gray-100'}`}
        >
          <MapPin size={20} />
          {isOpen && <span className="ml-3">Saved Places</span>}
        </button>
        <button 
          onClick={() => onTabChange('tags')}
          className={`flex items-center p-2 rounded ${activeTab === 'tags' ? 'bg-blue-100 text-blue-600' : 'hover:bg-gray-100'}`}
        >
          <Tag size={20} />
          {isOpen && <span className="ml-3">Manage Tags</span>}
        </button>
      </div>

      {/* Content */}
      {isOpen && (
        <div className="flex-1 p-4 overflow-y-auto">
          {activeTab === 'search' && (
            <SearchPanel 
              pois={pois}
              selectedTags={selectedTags}
              onTagToggle={onTagToggle}
            />
          )}
          {activeTab === 'saved' && (
            <SavedPlacesPanel pois={pois} />
          )}
          {activeTab === 'tags' && (
            <TagsPanel 
              selectedTags={selectedTags}
              onTagToggle={onTagToggle}
            />
          )}
        </div>
      )}
    </div>
  );
};