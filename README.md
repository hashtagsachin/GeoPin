# GeoPin

A web-based application for managing points of interest, built with modern web technologies.

## Tech Stack

- Frontend: React
- Backend: Spring Boot
- Database: PostgreSQL with PostGIS
- Hosting: Digital Ocean

## Project Structure

This application follows a modern web architecture with a clear separation between frontend and backend:

### Backend (Spring Boot)
- Models: POI (Point of Interest) and Tag entities
- Controllers: REST endpoints for managing POIs and Tags
- Services: Business logic layer
- Repositories: Data access layer with spatial query support

### Frontend (React)
- Coming soon

## Testing Strategy

GeoPin employs a comprehensive testing approach using TestContainers and Docker to ensure reliable spatial data handling. Our tests verify:

- Spatial Queries: Distance-based and bounding box searches using real London landmarks
- Tag Management: Hierarchical organization of points of interest
- Data Integrity: Proper handling of geographic coordinates and metadata

For detailed information about our testing approach, see [Testing Documentation](Learning%20Docs/Testing.md).

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven
- Node.js and npm (for frontend development)
- PostgreSQL with PostGIS extension
- Docker (for running tests)

### Setup Instructions
1. Clone the repository
2. Configure database connection in `application.properties`
3. Run the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Run tests:
   ```bash
   ./mvnw test
   ```
   Note: Docker must be running for tests to execute successfully

## Development Status
Currently in active development. Check back for updates!