import React, { useState, useEffect } from 'react';
import { Search, MapPin, Tag, Menu } from 'lucide-react';
import MapComponent from '../Map/MapComponent';
import SearchPanel from './SearchPanel';
import { poiService } from '../../services/api';
import './DashboardLayout.css';

const DashboardLayout = () => {
  const [sidebarOpen, setSidebarOpen] = useState(true);
  const [activeTab, setActiveTab] = useState('search');
  const [pois, setPois] = useState([]);
  const [filteredPois, setFilteredPois] = useState([]);
  const [selectedTags, setSelectedTags] = useState([]);
  
  useEffect(() => {
    const loadPOIs = async () => {
      try {
        const loadedPois = await poiService.getAllPOIs();
        setPois(loadedPois);
        setFilteredPois(loadedPois);
      } catch (error) {
        console.error('Error loading POIs:', error);
      }
    };
    
    loadPOIs();
  }, []);

  useEffect(() => {
    if (selectedTags.length === 0) {
      setFilteredPois(pois);
    } else {
      const filtered = pois.filter(poi => 
        poi.tags.some(tag => selectedTags.includes(tag.name))
      );
      setFilteredPois(filtered);
    }
  }, [selectedTags, pois]);

  return (
    <div className="dashboard-container">
      <aside className={`sidebar ${sidebarOpen ? 'open' : 'closed'}`}>
        <div className="sidebar-header">
          {sidebarOpen && <h1>GeoPin</h1>}
          <button onClick={() => setSidebarOpen(!sidebarOpen)}>
            <Menu size={20} />
          </button>
        </div>
        
        <nav className="sidebar-nav">
          <button 
            className={`nav-button ${activeTab === 'search' ? 'active' : ''}`}
            onClick={() => setActiveTab('search')}
          >
            <Search size={20} />
            {sidebarOpen && <span className="nav-button-text">Search POIs</span>}
          </button>
          <button 
            className={`nav-button ${activeTab === 'saved' ? 'active' : ''}`}
            onClick={() => setActiveTab('saved')}
          >
            <MapPin size={20} />
            {sidebarOpen && <span className="nav-button-text">Saved Places</span>}
          </button>
          <button 
            className={`nav-button ${activeTab === 'tags' ? 'active' : ''}`}
            onClick={() => setActiveTab('tags')}
          >
            <Tag size={20} />
            {sidebarOpen && <span className="nav-button-text">Manage Tags</span>}
          </button>
        </nav>

        {sidebarOpen && (
          <div className="sidebar-content">
            {activeTab === 'search' && (
              <SearchPanel 
                pois={pois}
                selectedTags={selectedTags}
                onTagToggle={(tag) => setSelectedTags(prev => 
                  prev.includes(tag) 
                    ? prev.filter(t => t !== tag)
                    : [...prev, tag]
                )}
              />
            )}
          </div>
        )}
      </aside>
      
      <div className="map-container">
        <MapComponent 
          markers={filteredPois.map(poi => ({
            id: poi.id,
            position: {
              lat: poi.latitude,
              lng: poi.longitude
            },
            name: poi.name,
            description: poi.description,
            tags: poi.tags.map(tag => tag.name),
            status: poi.status
          }))}
        />
      </div>
    </div>
  );
};

export default DashboardLayout;