# Architectural Simplification: From PostGIS to Pure Java

## Background and Decision Making

When building a geospatial application, it's tempting to reach for powerful tools like PostGIS right from the start. However, our experience with GeoPin taught us an important lesson in software architecture: sometimes, simpler solutions are not just adequate, but preferable.

### Why We Simplified

Initially, we implemented PostGIS for its robust spatial querying capabilities. However, we realized that our use case - a personal location bookmarking application with an expected scale of less than 200 points of interest - didn't justify the added complexity. This realization led to a significant architectural change on January 20, 2025.

### Benefits of Simplification

The decision to simplify our architecture has brought several advantages. First, we've reduced complexity significantly by eliminating special database extensions and simplifying our database schema to use standard Double fields for coordinates. This makes local development setup much easier and allows us to use standard H2 databases for testing.

Second, we've gained better understanding of our codebase through direct implementation of geographical calculations. Having clear, documented Java code for spatial operations makes the codebase more accessible for future maintenance and helps developers understand exactly how distance calculations work.

Third, we've found that for our scale of less than 200 POIs, in-memory calculations are more than sufficient. There's no noticeable performance impact for typical user operations, and we've eliminated the overhead of maintaining a specialized database extension.

## Technical Implementation

### Coordinate Storage

We now store coordinates as simple Double fields in our POI entity. These fields use the WGS84 coordinate system (the same system used by GPS and Google Maps), where latitude ranges from -90 to +90 degrees and longitude ranges from -180 to +180 degrees. This approach is straightforward, easy to understand, and compatible with any standard database.

### Geographical Calculations

The heart of our simplified architecture is the GeoCalculator utility class. This class provides two main functions that handle all our spatial operation needs:

1. The distance calculation method uses the Haversine formula to compute the great-circle distance between two points on Earth's surface. This formula provides excellent accuracy for most practical purposes, typically within 0.3% error margin. This is more than sufficient for our use case of finding nearby points of interest.

2. The bounding box check method determines if a point falls within a rectangular geographic area. This is particularly useful for area-based searches, such as finding all points of interest visible in the current map view.

These pure Java implementations give us complete control over the calculations and make the code more portable since it doesn't depend on database-specific features.

### Query Implementation

Instead of using PostGIS spatial queries, we now rely on standard JPA repository methods combined with our GeoCalculator. While this means we perform geographic calculations at the application layer rather than the database layer, the performance impact is negligible for our scale of usage.

Our repository layer focuses on basic data operations, while the service layer handles geographical calculations. This separation of concerns makes the code easier to understand and test.

## Limitations and Considerations

While our simplified approach works well for our specific use case, it's important to understand its limitations:

When searching near the international date line (180°/-180° longitude), special care must be taken as points might be geographically close but numerically distant. Our current implementation treats these as separate regions, which is acceptable for our use case but might need attention if global coverage becomes important.

The Haversine formula we use provides good accuracy for most uses but doesn't account for Earth's ellipsoid shape. For our use case of personal location bookmarking, this level of precision is more than adequate - the difference would typically be less than 0.3% compared to more complex calculations.

While our current implementation is perfect for personal use, applications requiring thousands of POIs or complex spatial queries might benefit from PostGIS's specialized indexing and query capabilities. It's important to understand that our architectural choice was driven by our specific requirements and scale.

## Testing Strategy

One significant advantage of our simplified architecture is that testing becomes more straightforward. We no longer need specialized PostGIS test containers, and we can use a standard H2 in-memory database for all our tests.

Our testing strategy now focuses on three main areas:

1. We directly test the GeoCalculator methods to ensure accurate distance calculations and correct bounding box checks. We use real-world landmarks with known distances to verify our calculations.

2. We test our repositories using H2 in-memory database, which is faster and simpler than setting up a PostGIS test container. This makes our tests more reliable and easier to run in any environment.

3. We have dedicated tests for edge cases like calculations near the poles and the international date line, ensuring our simplified approach handles these special cases correctly.

## Learning Outcomes

This architectural change has taught us valuable lessons about software design that extend beyond just geographical calculations:

First, we learned to start with the simplest solution that meets our needs. While it's tempting to use powerful tools like PostGIS, it's important to evaluate whether their complexity is justified by your specific requirements.

Second, we gained a deeper appreciation for how different scales often require different solutions. While PostGIS would be essential for applications handling thousands or millions of locations, our simple Java implementation is perfect for our scale of hundreds of locations.

Third, we learned the importance of challenging our assumptions about architectural decisions. Just because a technology is powerful and popular doesn't mean it's the right choice for every project. Sometimes, a simpler solution is not just adequate, but actually better.

Finally, we've seen firsthand how simpler solutions often lead to better maintainability. By removing the complexity of PostGIS, we've made our codebase more accessible, easier to test, and simpler to understand.

Remember: The best architecture is often the one that solves your specific problem without unnecessary complexity.