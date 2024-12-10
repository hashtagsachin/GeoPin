# GeoPin - Web Application Project

## Project Overview

GeoPin is a personal web-based application designed to help users record and organize their points of interest (POIs). The application grew from a need to efficiently manage and recall interesting locations discovered through various means - whether it's a restaurant seen on social media, a cool building spotted during a walk, or any other noteworthy location. Unlike traditional note-taking apps, GeoPin provides a map-based interface with powerful search and categorization capabilities.

## User Stories

### Primary Use Cases
1. **Social Media Discovery**: Save interesting locations found on social media platforms for future visits
   - Quick capture of place names and locations
   - Ability to add source references and notes
   - Easy categorization through tags

2. **On-the-Go Discovery**: Quickly mark interesting locations encountered during daily activities
   - Fast location pinning based on current position
   - Minimal initial input required
   - Option to add more details later

3. **Location Recall**: Efficiently find saved locations based on various criteria
   - Search by location area (e.g., "around London")
   - Filter by tags (e.g., "ramen restaurants")
   - Sort by various factors (distance, date added, etc.)

## Technical Implementation

### Current Progress

We have established the basic project structure following Spring Boot best practices:

1. **Package Structure** (com.geopin):
   - model: POI and Tag entities
   - repository: Database interaction interfaces
   - service: Business logic implementation
   - controller: REST API endpoints

2. **Technology Stack**:
   - Backend: Spring Boot 3.4.0 with Java 17
   - Database: PostgreSQL with PostGIS for geographical data
   - Frontend: React (planned)
   - Hosting: Digital Ocean (planned)

### Next Steps

1. **Data Model Implementation**:
   - Complete POI entity with geographical coordinates support
   - Implement Tag system for flexible categorization
   - Set up proper relationships between entities

2. **Database Setup**:
   - Configure PostgreSQL with PostGIS extension
   - Implement repository layer with spatial query support
   - Set up proper indexing for geographical queries

3. **Business Logic Layer**:
   - Implement service layer for POI management
   - Add support for custom tag creation
   - Create location-based search functionality

4. **API Development**:
   - Design RESTful API endpoints
   - Implement CRUD operations for POIs
   - Add search and filter endpoints

## Future Enhancements

While the initial implementation focuses on personal use, future developments may include:
1. User authentication for multi-user support
2. Mobile-responsive design
3. Integration with popular mapping services
4. Advanced geographical queries and visualization
5. Social sharing features
6. Offline access capabilities
7. Import/export functionality

## Development Process

We are following an iterative development approach:
1. ✓ Project setup and structure
2. ⟳ Core data model implementation (In Progress)
3. □ Database integration
4. □ Business logic implementation
5. □ API development
6. □ Frontend development
7. □ Testing and refinement
8. □ Deployment

This project serves both as a practical tool for personal use and as a learning experience in full-stack development, with potential for future commercialization.