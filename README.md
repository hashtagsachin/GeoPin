# GeoPin

A personal web application for managing points of interest (POIs) with an interactive map interface. Pin, organize, and search your favorite locations.

## Screenshots

[Coming soon]
- Main interface with map and sidebar
- POI creation/editing interface
- Search and filtering view
- Tag management view

## Features

- **Interactive Map Management**
  - Create location markers with custom details
  - Find your current location
  - Smart map transitions with viewport-aware animations
  - Support for both custom markers and Google Places integration

- **Location Organization**
  - Tag-based categorization
  - Status tracking (Active/Archived/Temporary)
  - Custom descriptions and notes
  - Search by name, description, or tags

- **Search & Filter**
  - Radius-based location search using Haversine formula
  - Filter POIs by tags
  - Search through saved locations
  - Interactive search results with map integration

- **Responsive Design**
  - Collapsible sidebar for better map visibility
  - Adaptive layout for different screen sizes
  - Smooth transitions and animations

## Tech Stack

### Frontend
- React 19
- Google Maps JavaScript API
- Modern UI with CSS Modules
- Key libraries:
  - @react-google-maps/api for map integration
  - lucide-react for icons
  - react-scripts 5.0.1

### Backend
- Spring Boot 3.4.0
- Java 17
- PostgreSQL
- JUnit 5 for testing
- RESTful API design

## Setup

### Prerequisites
- Node.js (v16 or higher)
- Java 17 JDK
- PostgreSQL 14 or higher
- Google Maps API key

### Database Setup
1. Create PostgreSQL database:
   ```sql
   CREATE DATABASE geopin;
   CREATE USER geopin_user WITH PASSWORD '1234';
   GRANT ALL PRIVILEGES ON DATABASE geopin TO geopin_user;
   ```

### Backend Setup
1. Clone the repository:
   ```bash
   git clone [repository-url]
   cd geopin
   ```

2. Configure application.properties:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/geopin
   spring.datasource.username=geopin_user
   spring.datasource.password=1234
   ```

3. Run the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```
   The backend will start on `http://localhost:8080`

### Frontend Setup
1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Create a `.env` file:
   ```env
   REACT_APP_GOOGLE_MAPS_API_KEY=your_google_maps_api_key
   ```

3. Install dependencies and start the development server:
   ```bash
   npm install
   npm start
   ```
   The frontend will start on `http://localhost:3000`

## API Endpoints

### POIs
- `GET /api/pois` - Get all POIs
- `POST /api/pois` - Create a new POI
- `GET /api/pois/{id}` - Get POI by ID
- `PUT /api/pois/{id}` - Update a POI
- `DELETE /api/pois/{id}` - Delete a POI
- `GET /api/pois/search/radius` - Search POIs within radius
- `GET /api/pois/search/bounds` - Search POIs within bounds

### Tags
- `GET /api/tags` - Get all tags
- `POST /api/tags` - Create a new tag
- `DELETE /api/tags/{id}` - Delete a tag
- `GET /api/tags/search/{tagName}/pois` - Get POIs by tag name

## Deployment

Plans are in place to host this application on Digital Ocean, utilizing their App Platform for both the frontend and backend components. The PostgreSQL database will be hosted using Digital Ocean's managed database service.

## Status

This is a personal project that is actively maintained. The current version implements core location management features with a focus on usability and performance. Future updates may include additional features based on usage and requirements.

---

For any questions or issues, please open a GitHub issue or reach out directly.