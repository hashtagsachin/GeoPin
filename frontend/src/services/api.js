// api.js
const API_BASE_URL = 'http://localhost:8080/api';

// Helper function for handling API responses
const handleResponse = async (response) => {
    if (!response.ok) {
      // Try to parse error response as JSON, but handle cases where it might not be JSON
      try {
        const error = await response.json();
        throw new Error(error.message || 'An error occurred');
      } catch (e) {
        throw new Error('An error occurred while processing the request');
      }
    }
    
    // For DELETE operations or other empty responses, don't try to parse JSON
    if (response.status === 204 || response.headers.get('content-length') === '0') {
      return null;
    }
    
    // For all other cases, parse the JSON response
    try {
      return await response.json();
    } catch (e) {
      throw new Error('Invalid response format from server');
    }
  };
  

// POI-related API calls
export const poiService = {
  // Create a new POI
  async createPOI(poiData) {
    const response = await fetch(`${API_BASE_URL}/pois`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(poiData),
    });
    return handleResponse(response);
  },

  // Get all POIs
  async getAllPOIs() {
    const response = await fetch(`${API_BASE_URL}/pois`);
    return handleResponse(response);
  },

  // Get POI by ID
  async getPOIById(id) {
    const response = await fetch(`${API_BASE_URL}/pois/${id}`);
    return handleResponse(response);
  },

  // Update a POI
  async updatePOI(id, poiData) {
    const response = await fetch(`${API_BASE_URL}/pois/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(poiData),
    });
    return handleResponse(response);
  },

  // Delete a POI
  async deletePOI(id) {
    const response = await fetch(`${API_BASE_URL}/pois/${id}`, {
      method: 'DELETE',
    });
    return handleResponse(response);
  },

  // Find POIs within distance
  async findPOIsWithinDistance(lat, lng, distance) {
    const response = await fetch(
      `${API_BASE_URL}/pois/search/within?lat=${lat}&lng=${lng}&distance=${distance}`
    );
    return handleResponse(response);
  },

  // Find POIs within bounding box
  async findPOIsInBoundingBox(minLat, minLng, maxLat, maxLng) {
    const response = await fetch(
      `${API_BASE_URL}/pois/search/box?minLat=${minLat}&minLng=${minLng}&maxLat=${maxLat}&maxLng=${maxLng}`
    );
    return handleResponse(response);
  }
};

// Tag-related API calls
export const tagService = {
  // Create a new tag
  async createTag(tagData) {
    const response = await fetch(`${API_BASE_URL}/tags`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(tagData),
    });
    return handleResponse(response);
  },

  // Get all tags
  async getAllTags() {
    const response = await fetch(`${API_BASE_URL}/tags`);
    return handleResponse(response);
  },

  // Delete a tag
  async deleteTag(id) {
    const response = await fetch(`${API_BASE_URL}/tags/${id}`, {
      method: 'DELETE',
    });
    return handleResponse(response);
  },

  // Find POIs by tag
  async findPOIsByTag(tagName) {
    const response = await fetch(`${API_BASE_URL}/pois/search/tag/${tagName}`);
    return handleResponse(response);
  }
};

// Sample POI data structure for reference
/*
const samplePOI = {
  name: "Trafalgar Square",
  location: {
    type: "Point",
    coordinates: [-0.128069, 51.508039]  // [longitude, latitude]
  },
  status: "ACTIVE",
  tags: ["tourist", "landmark"],
  notes: "Famous square in central London"
};
*/