# Project Management Bounded Context

This bounded context manages client accounts and their projects within the Pragma Knowledge Tracker system.

## Architecture

This bounded context follows **Hexagonal Architecture (Ports & Adapters)** with **Domain-Driven Design** principles.

### Layer Structure

```
projectmanagement/
├── domain/                          # Domain Layer (Pure Java, no frameworks)
│   ├── model/                       # Entities, Value Objects, Aggregates
│   │   ├── Account.java            # Aggregate Root
│   │   ├── AccountId.java          # Value Object
│   │   ├── AccountStatus.java      # Value Object (enum)
│   │   ├── Project.java            # Aggregate Root
│   │   ├── ProjectId.java          # Value Object
│   │   ├── ProjectStatus.java      # Value Object (enum)
│   │   ├── ProjectType.java        # Value Object (enum)
│   │   └── DateRange.java          # Value Object
│   ├── repository/                  # Repository Ports (interfaces)
│   │   ├── AccountRepository.java
│   │   └── ProjectRepository.java
│   └── exception/                   # Domain Exceptions
│       ├── AccountNotFoundException.java
│       ├── ProjectNotFoundException.java
│       └── DuplicateAccountNameException.java
│
├── application/                     # Application Layer (Use Cases)
│   ├── usecase/                    # Application Services
│   │   ├── AccountService.java
│   │   └── ProjectService.java
│   ├── dto/                        # Data Transfer Objects
│   │   ├── AccountDTO.java
│   │   ├── ProjectDTO.java
│   │   ├── CreateAccountCommand.java
│   │   └── CreateProjectCommand.java
│   └── mapper/                     # DTO Mappers
│       ├── AccountMapper.java
│       └── ProjectMapper.java
│
└── infrastructure/                  # Infrastructure Layer (Framework code)
    ├── web/                        # REST Controllers (Input Adapters)
    │   ├── AccountController.java
    │   ├── ProjectController.java
    │   └── GlobalExceptionHandler.java
    ├── persistence/                # JPA Persistence (Output Adapters)
    │   ├── entity/                 # JPA Entities
    │   │   ├── JpaAccountEntity.java
    │   │   └── JpaProjectEntity.java
    │   ├── adapter/                # Repository Implementations
    │   │   ├── JpaAccountRepositoryAdapter.java
    │   │   └── JpaProjectRepositoryAdapter.java
    │   ├── mapper/                 # Entity Mappers
    │   │   ├── AccountEntityMapper.java
    │   │   └── ProjectEntityMapper.java
    │   ├── JpaAccountSpringRepository.java
    │   └── JpaProjectSpringRepository.java
    └── config/                     # Spring Configuration
        └── ProjectManagementConfig.java
```

## Domain Model

### Aggregates

#### Account Aggregate
**Root Entity:** `Account`

**Invariants:**
- Account name must be unique
- Account name cannot be null or blank
- Region is optional but if provided must not be blank

**Business Methods:**
- `activate()` - Activate an inactive account
- `deactivate()` - Deactivate an active account
- `suspend()` - Suspend an account
- `updateName(String)` - Update account name
- `updateRegion(String)` - Update account region
- `addAttribute(String, String)` - Add custom attribute
- `removeAttribute(String)` - Remove custom attribute

#### Project Aggregate
**Root Entity:** `Project`

**Invariants:**
- Project must belong to an Account
- Project name cannot be null or blank
- Active projects must have a start date
- End date must be after start date if provided

**Business Methods:**
- `activate()` - Activate the project
- `deactivate()` - Deactivate the project
- `complete()` - Mark project as completed
- `putOnHold()` - Put project on hold
- `updateName(String)` - Update project name
- `updateType(ProjectType)` - Update project type
- `updateDateRange(DateRange)` - Update project dates
- `addAttribute(String, String)` - Add custom attribute
- `removeAttribute(String)` - Remove custom attribute

### Value Objects

- **AccountId** - Account identifier
- **ProjectId** - Project identifier
- **AccountStatus** - ACTIVE, INACTIVE, SUSPENDED
- **ProjectStatus** - ACTIVE, INACTIVE, COMPLETED, ON_HOLD
- **ProjectType** - ABIERTO (Time & Materials), CERRADO (Fixed Price), N/A
- **DateRange** - Start and end dates with validation

## REST API Endpoints

### Account Endpoints

```
POST   /api/accounts                 - Create a new account
GET    /api/accounts                 - Get all accounts
GET    /api/accounts?status=ACTIVE   - Get accounts by status
GET    /api/accounts?region=Colombia - Get accounts by region
GET    /api/accounts/{id}            - Get account by ID
PUT    /api/accounts/{id}/name       - Update account name
PUT    /api/accounts/{id}/activate   - Activate account
PUT    /api/accounts/{id}/deactivate - Deactivate account
DELETE /api/accounts/{id}            - Delete account
```

### Project Endpoints

```
POST   /api/projects                  - Create a new project
GET    /api/projects                  - Get all projects
GET    /api/projects?accountId=1      - Get projects by account
GET    /api/projects?status=ACTIVE    - Get projects by status
GET    /api/projects/{id}             - Get project by ID
PUT    /api/projects/{id}/name        - Update project name
PUT    /api/projects/{id}/activate    - Activate project
PUT    /api/projects/{id}/complete    - Complete project
DELETE /api/projects/{id}             - Delete project
```

## Example Usage

### Create an Account
```bash
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Bancolombia",
    "region": "Latin America",
    "status": "ACTIVE",
    "attributes": {
      "industry": "Banking",
      "size": "Enterprise"
    }
  }'
```

### Create a Project
```bash
curl -X POST http://localhost:8080/api/projects \
  -H "Content-Type: application/json" \
  -d '{
    "accountId": 1,
    "name": "Core Banking Modernization",
    "status": "ACTIVE",
    "startDate": "2024-01-15T00:00:00",
    "type": "ABIERTO",
    "attributes": {
      "priority": "high",
      "budget": "large"
    }
  }'
```

### Get All Active Projects
```bash
curl http://localhost:8080/api/projects?status=ACTIVE
```

### Get Projects by Account
```bash
curl http://localhost:8080/api/projects?accountId=1
```

## Testing

Run the unit tests:
```bash
./gradlew test --tests "com.pragma.vc.tracker.projectmanagement.domain.model.*"
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

2. **Hexagonal Architecture**
   - Domain defines interfaces (ports)
   - Infrastructure implements adapters
   - Dependency inversion: Infrastructure depends on Domain

3. **Value Objects**
   - Immutable and self-validating
   - Used for IDs, status enums, and date ranges
   - Enforce domain invariants

4. **Aggregate Roots**
   - Account and Project are separate aggregates
   - Each controls its own consistency boundary
   - References between aggregates use IDs only

5. **Repository Pattern**
   - Domain defines repository interfaces
   - Infrastructure provides JPA implementations
   - Converts between domain and persistence models

## Database Mapping

The infrastructure layer maps domain models to database tables:
- `Account` → `"Account"` table
- `Project` → `"Project"` table

Attributes (key-value pairs) are serialized to JSON strings in the database.

## Next Steps

Future enhancements for this bounded context:
- Add domain events (AccountCreated, ProjectCompleted, etc.)
- Implement pagination for list queries
- Add search and filtering capabilities
- Add validation annotations for REST endpoints
- Create integration tests with test containers