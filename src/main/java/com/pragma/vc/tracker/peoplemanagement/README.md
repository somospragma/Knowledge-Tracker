# People Management Bounded Context

This bounded context manages Pragma SA employees (Pragmatics) and their organizational structure (Chapters) within the Knowledge Tracker system.

## Architecture

This bounded context follows **Hexagonal Architecture (Ports & Adapters)** with **Domain-Driven Design** principles.

### Layer Structure

```
peoplemanagement/
├── domain/                          # Domain Layer (Pure Java, no frameworks)
│   ├── model/                       # Entities, Value Objects, Aggregates
│   │   ├── Chapter.java            # Aggregate Root
│   │   ├── ChapterId.java          # Value Object
│   │   ├── Pragmatic.java          # Aggregate Root
│   │   ├── PragmaticId.java        # Value Object
│   │   ├── Email.java              # Value Object
│   │   └── PragmaticStatus.java    # Value Object (enum)
│   ├── repository/                  # Repository Ports (interfaces)
│   │   ├── ChapterRepository.java
│   │   └── PragmaticRepository.java
│   └── exception/                   # Domain Exceptions
│       ├── ChapterNotFoundException.java
│       ├── PragmaticNotFoundException.java
│       ├── DuplicateChapterNameException.java
│       └── DuplicateEmailException.java
│
├── application/                     # Application Layer (Use Cases)
│   ├── usecase/                    # Application Services
│   │   ├── ChapterService.java
│   │   └── PragmaticService.java
│   ├── dto/                        # Data Transfer Objects
│   │   ├── ChapterDTO.java
│   │   ├── PragmaticDTO.java
│   │   ├── CreateChapterCommand.java
│   │   └── CreatePragmaticCommand.java
│   └── mapper/                     # DTO Mappers
│       ├── ChapterMapper.java
│       └── PragmaticMapper.java
│
└── infrastructure/                  # Infrastructure Layer (Framework code)
    ├── web/                        # REST Controllers (Input Adapters)
    │   ├── ChapterController.java
    │   ├── PragmaticController.java
    │   └── PeopleManagementExceptionHandler.java
    ├── persistence/                # JPA Persistence (Output Adapters)
    │   ├── entity/                 # JPA Entities
    │   │   ├── JpaChapterEntity.java
    │   │   └── JpaPragmaticEntity.java
    │   ├── adapter/                # Repository Implementations
    │   │   ├── JpaChapterRepositoryAdapter.java
    │   │   └── JpaPragmaticRepositoryAdapter.java
    │   ├── mapper/                 # Entity Mappers
    │   │   ├── ChapterEntityMapper.java
    │   │   └── PragmaticEntityMapper.java
    │   ├── JpaChapterSpringRepository.java
    │   └── JpaPragmaticSpringRepository.java
    └── config/                     # Spring Configuration
        └── PeopleManagementConfig.java
```

## Domain Model

### Aggregates

#### Chapter Aggregate
**Root Entity:** `Chapter`

**Represents:** An organizational unit or team within Pragma SA (e.g., Backend Development, Frontend Development, DevOps)

**Invariants:**
- Chapter name must be unique
- Chapter name cannot be null or blank

**Business Methods:**
- `updateName(String)` - Update chapter name

#### Pragmatic Aggregate
**Root Entity:** `Pragmatic`

**Represents:** A Pragma SA employee

**Invariants:**
- Email must be unique across all Pragmatics
- Email must be in valid format
- First and last names are required
- Active Pragmatics must belong to a Chapter
- Only active Pragmatics can be assigned to projects

**Business Methods:**
- `activate()` - Activate the pragmatic (requires chapter assignment)
- `deactivate()` - Deactivate the pragmatic
- `putOnLeave()` - Put pragmatic on leave
- `updateEmail(Email)` - Update email address
- `updateName(String, String)` - Update first and last name
- `assignToChapter(ChapterId)` - Assign to a chapter

### Value Objects

- **ChapterId** - Chapter identifier
- **PragmaticId** - Pragmatic identifier
- **Email** - Valid email address with format validation
  - Self-validating with regex pattern
  - Case-insensitive
  - Provides domain() and localPart() methods
- **PragmaticStatus** - ACTIVE, INACTIVE, ON_LEAVE
  - `isActive()` - Check if pragmatic is active
  - `canBeAssignedToProjects()` - Check if eligible for project assignment

## REST API Endpoints

### Chapter Endpoints

```
POST   /api/chapters              - Create a new chapter
GET    /api/chapters              - Get all chapters
GET    /api/chapters/{id}         - Get chapter by ID
PUT    /api/chapters/{id}/name    - Update chapter name
DELETE /api/chapters/{id}         - Delete chapter
```

### Pragmatic Endpoints

```
POST   /api/pragmatics                 - Create a new pragmatic
GET    /api/pragmatics                 - Get all pragmatics
GET    /api/pragmatics?chapterId=1     - Get pragmatics by chapter
GET    /api/pragmatics?status=ACTIVE   - Get pragmatics by status
GET    /api/pragmatics/{id}            - Get pragmatic by ID
PUT    /api/pragmatics/{id}/email      - Update pragmatic email
PUT    /api/pragmatics/{id}/chapter    - Assign pragmatic to chapter
PUT    /api/pragmatics/{id}/activate   - Activate pragmatic
PUT    /api/pragmatics/{id}/deactivate - Deactivate pragmatic
PUT    /api/pragmatics/{id}/on-leave   - Put pragmatic on leave
DELETE /api/pragmatics/{id}            - Delete pragmatic
```

## Example Usage

### Create a Chapter
```bash
curl -X POST http://localhost:8080/api/chapters \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Backend Development"
  }'
```

### Create a Pragmatic
```bash
curl -X POST http://localhost:8080/api/pragmatics \
  -H "Content-Type: application/json" \
  -d '{
    "chapterId": 1,
    "email": "juan.perez@pragma.com.co",
    "firstName": "Juan",
    "lastName": "Pérez",
    "status": "ACTIVE"
  }'
```

### Get All Active Pragmatics
```bash
curl http://localhost:8080/api/pragmatics?status=ACTIVE
```

### Get Pragmatics by Chapter
```bash
curl http://localhost:8080/api/pragmatics?chapterId=1
```

### Assign Pragmatic to Different Chapter
```bash
curl -X PUT http://localhost:8080/api/pragmatics/1/chapter \
  -H "Content-Type: application/json" \
  -d '{
    "chapterId": 2
  }'
```

### Put Pragmatic on Leave
```bash
curl -X PUT http://localhost:8080/api/pragmatics/1/on-leave
```

## Testing

Run the unit tests:
```bash
./gradlew test --tests "com.pragma.vc.tracker.peoplemanagement.domain.model.*"
```

Run all tests:
```bash
./gradlew test
```

## Key Design Principles

1. **Domain Layer Independence**
   - No framework dependencies (no Spring, no JPA annotations)
   - Pure business logic
   - Rich domain model with behavior

2. **Email Value Object**
   - Self-validating with regex pattern
   - Immutable
   - Normalized to lowercase
   - Provides utility methods (getDomain(), getLocalPart())

3. **Hexagonal Architecture**
   - Domain defines interfaces (ports)
   - Infrastructure implements adapters
   - Dependency inversion: Infrastructure depends on Domain

4. **Value Objects**
   - Email with validation
   - Immutable IDs
   - Status enums with business methods

5. **Aggregate Roots**
   - Chapter and Pragmatic are separate aggregates
   - Each controls its own consistency boundary
   - References between aggregates use IDs only

6. **Business Rules Enforcement**
   - Active Pragmatics must have a Chapter
   - Email uniqueness enforced
   - Status transitions controlled by domain logic

## Database Mapping

The infrastructure layer maps domain models to database tables:
- `Chapter` → `"Chapter"` table
- `Pragmatic` → `"Pragmatic"` table

Email values are stored as lowercase strings.

## Domain Invariants

### Chapter
- Name must be unique
- Name cannot be blank
- Name max length: 255 characters

### Pragmatic
- Email must be unique
- Email must be valid format
- Active status requires chapter assignment
- First name max length: 100 characters
- Last name max length: 100 characters

## Status Transitions

```
INACTIVE ──activate()──> ACTIVE
ACTIVE ──deactivate()──> INACTIVE
ACTIVE ──putOnLeave()──> ON_LEAVE
ON_LEAVE ──activate()──> ACTIVE
ON_LEAVE ──deactivate()──> INACTIVE
```

**Business Rules:**
- Can only activate if assigned to a Chapter
- Only ACTIVE pragmatics can be assigned to projects
- Cannot activate an already active pragmatic

## Next Steps

Future enhancements for this bounded context:
- Add domain events (PragmaticHired, PragmaticLeftCompany, etc.)
- Implement skills/expertise tracking per pragmatic
- Add team composition queries
- Add pagination for list queries
- Create integration tests
- Add performance metrics (pragmatics per chapter, etc.)