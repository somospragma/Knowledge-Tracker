# API Documentation Setup - SpringDoc OpenAPI

## Summary

This document describes the API versioning and OpenAPI documentation setup for the Knowledge Tracker application.

## What's Been Completed

### 1. ✅ Dependencies Added

**File:** `build.gradle`

```gradle
// SpringDoc OpenAPI for API documentation (Swagger)
implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0'
implementation 'org.springframework.boot:spring-boot-starter-validation'
```

### 2. ✅ API Versioning Applied

All REST controllers have been updated to use `/api/v1` prefix:

- `/api/v1/accounts` - Account management
- `/api/v1/projects` - Project management
- `/api/v1/territories` - Territory management
- `/api/v1/pragmatics` - Pragmatic (employee) management
- `/api/v1/chapters` - Chapter management
- `/api/v1/knowledge` - Knowledge catalog
- `/api/v1/categories` - Knowledge categories
- `/api/v1/levels` - Proficiency levels
- `/api/v1/applied-knowledge` - Applied knowledge (Core)

### 3. ✅ OpenAPI Configuration Created

**File:** `src/main/java/com/pragma/vc/tracker/infrastructure/config/OpenApiConfig.java`

Configured with:
- API title: "Pragma Knowledge Tracker API"
- Version: v1
- Contact information
- Server URLs
- Comprehensive API description

**File:** `src/main/resources/application.yml`

```yaml
springdoc:
  api-docs:
    path: /v3/api-docs
    enabled: true
  swagger-ui:
    path: /swagger-ui.html
    enabled: true
    tags-sorter: alpha
    operations-sorter: alpha
  default-consumes-media-type: application/json
  default-produces-media-type: application/json
```

### 4. ✅ OpenAPI Annotations Added

Added comprehensive documentation to controllers:

**Example controllers updated:**
- `AccountController` - Full OpenAPI annotations
- `KnowledgeController` - Full OpenAPI annotations

**Annotations used:**
- `@Tag` - Controller-level documentation
- `@Operation` - Endpoint description
- `@ApiResponses` - HTTP response documentation
- `@Parameter` - Request parameter documentation
- `@Schema` - DTO schema references

## Accessing API Documentation

Once the application is running, access the documentation at:

### Swagger UI (Interactive Documentation)
```
http://localhost:8080/swagger-ui.html
```
or
```
http://localhost:8080/swagger-ui/index.html
```

### OpenAPI JSON Specification
```
http://localhost:8080/v3/api-docs
```

### OpenAPI YAML Specification
```
http://localhost:8080/v3/api-docs.yaml
```

## Example API Endpoints

### Accounts API
```bash
# Get all accounts
GET http://localhost:8080/api/v1/accounts

# Get account by ID
GET http://localhost:8080/api/v1/accounts/{id}

# Create account
POST http://localhost:8080/api/v1/accounts
Content-Type: application/json

{
  "name": "Client Name",
  "territoryId": 1,
  "attributes": {}
}
```

### Knowledge API
```bash
# Get all knowledge items
GET http://localhost:8080/api/v1/knowledge

# Search knowledge by name
GET http://localhost:8080/api/v1/knowledge?search=Java

# Filter by category
GET http://localhost:8080/api/v1/knowledge?categoryId=1

# Filter by approval status
GET http://localhost:8080/api/v1/knowledge?status=Approved
```

## Adding OpenAPI Annotations to Remaining Controllers

To complete the OpenAPI documentation, add annotations to the remaining controllers:

### Pattern to Follow

```java
package com.pragma.vc.tracker.example.infrastructure.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/example")
@Tag(name = "Example", description = "Example API description")
public class ExampleController {

    @Operation(summary = "Create example", description = "Creates a new example entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully",
                    content = @Content(schema = @Schema(implementation = ExampleDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ExampleDTO> create(@RequestBody CreateCommand command) {
        // implementation
    }

    @Operation(summary = "Get by ID", description = "Retrieves example by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found",
                    content = @Content(schema = @Schema(implementation = ExampleDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExampleDTO> getById(
            @Parameter(description = "Example ID", required = true) @PathVariable Long id) {
        // implementation
    }
}
```

## Controllers to Update

Apply the OpenAPI annotations pattern to:

1. ✅ `AccountController` - Done
2. ✅ `KnowledgeController` - Done
3. `ProjectController`
4. `TerritoryController`
5. `PragmaticController`
6. `ChapterController`
7. `CategoryController`
8. `LevelController`
9. `AppliedKnowledgeController`

## Troubleshooting

### Swagger UI not loading

1. **Check application is running:**
   ```bash
   curl http://localhost:8080/actuator/health
   ```

2. **Check OpenAPI endpoint:**
   ```bash
   curl http://localhost:8080/v3/api-docs
   ```

3. **Check logs for errors:**
   ```bash
   ./gradlew bootRun
   ```

### Version Compatibility Issues

If you encounter `NoSuchMethodError` or similar errors:

1. **Check Spring Boot version compatibility:**
   - Spring Boot 3.5.x → SpringDoc OpenAPI 2.7.0+
   - Spring Boot 3.2.x → SpringDoc OpenAPI 2.3.0
   - Spring Boot 2.7.x → SpringDoc OpenAPI 1.7.0

2. **Update dependency in build.gradle:**
   ```gradle
   implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:VERSION'
   ```

3. **Clean and rebuild:**
   ```bash
   ./gradlew clean build
   ```

### API endpoints not showing in Swagger

1. **Ensure controllers have `@RestController` annotation**
2. **Verify `@RequestMapping` paths**
3. **Check component scanning includes controller packages**
4. **Add `@Tag` annotation to controller class**

## Benefits

### For Developers
- Interactive API testing via Swagger UI
- Automatic API documentation generation
- Type-safe contract definitions
- Request/response examples

### For Frontend Teams
- OpenAPI spec can generate client SDKs
- Clear API contracts
- Test endpoints without backend code

### For Documentation
- Always up-to-date API docs
- Standard OpenAPI 3.0 format
- Exportable JSON/YAML specifications

## Next Steps

1. **Complete OpenAPI annotations** for remaining 7 controllers
2. **Add request/response examples** to DTOs using `@Schema` annotations
3. **Configure API security documentation** (if using authentication)
4. **Add global error response schemas**
5. **Generate API client libraries** from OpenAPI spec for frontend

## Resources

- **SpringDoc OpenAPI Documentation:** https://springdoc.org/
- **OpenAPI Specification:** https://swagger.io/specification/
- **Swagger UI:** https://swagger.io/tools/swagger-ui/
- **Spring Boot Compatibility:** https://springdoc.org/#spring-boot-3-support

