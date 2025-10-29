# Pragma Knowledge Tracking System (Vigilancia)

## Description

The Pragma Knowledge Tracking System is a comprehensive platform designed to track and analyze technical knowledge that Pragma SA employees (Pragmatics) apply to client projects. The system provides visibility into the technical expertise utilized across projects, enabling better resource allocation, skills gap identification, and strategic decision-making.

**Key Features:**
- Track technical knowledge applied by Pragmatics across client projects
- Manage client accounts and project assignments
- Monitor skill proficiency levels (Beginner, Intermediate, Advanced, Expert)
- Categorize knowledge by type (Platform, Language, Framework, Tool, Technique)
- Support strategic resource allocation and training initiatives
- Provide clients with transparency into technical capabilities

---

## Business Context

### Business Goals
1. **Resource Optimization** - Track technical knowledge applied across all Pragma SA projects to enable better resource allocation based on Pragmatic expertise
2. **Skills Development** - Identify skills gaps and training needs across the organization
3. **Client Transparency** - Provide clients visibility into technical capabilities and expertise deployed on their projects
4. **Strategic Planning** - Support strategic decisions for technology investments and hiring based on knowledge utilization patterns
5. **Quality Assurance** - Ensure appropriate skill levels are assigned to project requirements

### Target Users
- **Pragma SA Management** - Resource allocation and strategic planning
- **Project Managers** - Skills visibility and team composition
- **Clients** - Transparency into technical capabilities
- **HR and Training Teams** - Skills development and training program planning
- **Pragmatics (Employees)** - Self-assessment and career development

### Domain Model

**Bounded Contexts:**
1. **Knowledge Application (Core)** - Track what knowledge Pragmatics apply to projects
2. **Project Management** - Manage client accounts and projects
3. **People Management** - Manage Pragma SA employees (Pragmatics)
4. **Knowledge Catalog** - Define technical knowledge categories

**Key Entities:**
- **Account** - Client organization
- **Project** - Client project with assigned Pragmatics
- **Pragmatic** - Pragma SA employee
- **Knowledge** - Technical skill/knowledge (platform, language, framework, tool, technique)
- **AppliedKnowledge** - Assignment of knowledge to project by pragmatic at a proficiency level

---

## Architecture

### Architectural Style
**Hexagonal Architecture (Ports & Adapters) with Domain-Driven Design**

The application follows a strict layered architecture that separates business logic from infrastructure concerns, ensuring maintainability, testability, and framework independence.

### Layer Structure

```
src/main/java/com/pragma/vc/tracker/
├── domain/                          # Domain Layer (Pure Java, no frameworks)
│   ├── model/                       # Entities, Value Objects, Aggregates
│   ├── service/                     # Domain Services
│   └── repository/                  # Repository interfaces (ports)
│
├── application/                     # Application Layer (Use Cases)
│   ├── usecase/                     # Application services, orchestration
│   └── port/                        # Input/Output ports for use cases
│
└── infrastructure/                  # Infrastructure Layer (Framework code)
    ├── adapter/
    │   ├── web/                     # REST Controllers (input adapters)
    │   ├── persistence/             # JPA Repositories (output adapters)
    │   └── messaging/               # Kafka producers/consumers (future)
    └── config/                      # Spring configuration classes
```

### Dependency Rules

```
Infrastructure Layer (Spring, JPA, REST)
    ↓ depends on
Application Layer (Use Cases)
    ↓ depends on
Domain Layer (Pure Java)
```

**Critical Principles:**
- Domain layer: NO Spring annotations, NO JPA, NO framework dependencies
- Application layer: Depends ONLY on domain interfaces (ports), NO Spring annotations
- Infrastructure layer: Implements ports, uses Spring framework
- Application services registered as Spring beans via `@Configuration` classes
- Transaction management handled at infrastructure layer (repository adapters)

---

## Technology Stack

### Core Technologies
- **Java** - 21 (LTS)
- **Spring Boot** - 3.5.6
- **Gradle** - 8.x with Gradle Wrapper
- **PostgreSQL** - 16 (production)
- **H2 Database** - In-memory (development/testing)

### Spring Boot Starters
- `spring-boot-starter-web` - REST API support
- `spring-boot-starter-data-jpa` - Data persistence with JPA
- `spring-boot-starter-actuator` - Application monitoring and health checks
- `spring-boot-devtools` - Development hot reloading

### Development Tools
- **Lombok** - Reduce boilerplate code (@Getter, @Setter, @Builder, etc.)
- **JUnit 5** - Unit and integration testing
- **Mockito** - Mocking framework for tests
- **AssertJ** - Fluent assertions for tests

### Database
- **PostgreSQL Driver** - Production database connectivity
- **Hibernate** - ORM (JPA implementation)

---

## Build & Run

### Prerequisites
- Java 21 or higher
- PostgreSQL 16 (for dev/test/prod profiles)
- Git

### Environment Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Knowledge-Tracker
   ```

2. **Configure environment variables**
   ```bash
   # Copy the example environment file
   cp .env.example .env

   # Edit .env with your configuration
   nano .env
   ```

3. **Create databases** (if using PostgreSQL)
   ```sql
   CREATE DATABASE knowledge_tracker_dev;
   CREATE DATABASE knowledge_tracker_test;
   CREATE DATABASE knowledge_tracker;  -- production
   ```

### Build Commands

```bash
# Build the entire project
./gradlew build

# Clean and rebuild
./gradlew clean build

# Build without tests
./gradlew build -x test

# Build executable JAR
./gradlew bootJar
```

### Run Commands

**Development Profile (dev)**
```bash
# Using default values from application-dev.yml
./gradlew bootRun --args='--spring.profiles.active=dev'

# With custom environment variables
DB_HOST=localhost DB_PORT=5432 DB_NAME=knowledge_tracker_dev \
DB_USERNAME=postgres DB_PASSWORD=postgres \
./gradlew bootRun --args='--spring.profiles.active=dev'

# Using .env file
set -a && source .env && set +a && ./gradlew bootRun --args='--spring.profiles.active=dev'
```

**Test Profile (test)**
```bash
./gradlew bootRun --args='--spring.profiles.active=test'
```

**Production Profile (prod)**
```bash
# IMPORTANT: Set production environment variables first!
PROD_DB_HOST=prod-db.example.com \
PROD_DB_PORT=5432 \
PROD_DB_NAME=knowledge_tracker \
PROD_DB_USERNAME=prod_user \
PROD_DB_PASSWORD=secure_password \
PROD_SERVER_PORT=8080 \
./gradlew bootRun --args='--spring.profiles.active=prod'
```

### Testing Commands

```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests ClassName

# Run specific test method
./gradlew test --tests ClassName.methodName

# Run tests in a package
./gradlew test --tests 'com.pragma.vc.tracker.*'

# Run tests with detailed output
./gradlew test --info

# Run all checks (tests + quality)
./gradlew check
```

### Environment Variables

The application uses environment variables for configuration across different profiles:

| Variable | Profile | Default | Description |
|----------|---------|---------|-------------|
| `DB_HOST` | dev | localhost | Database host |
| `DB_PORT` | dev | 5432 | Database port |
| `DB_NAME` | dev | knowledge_tracker_dev | Database name |
| `DB_USERNAME` | dev | postgres | Database username |
| `DB_PASSWORD` | dev | postgres | Database password |
| `SERVER_PORT` | dev | 8080 | Application server port |
| `TEST_DB_*` | test | (various) | Test environment database settings |
| `PROD_DB_*` | prod | (various) | Production environment database settings |

See `.env.example` for a complete list of available environment variables.

### Application Endpoints

Once running, the application will be available at:
- **Development**: http://localhost:8080
- **Test**: http://localhost:8081
- **Production**: http://localhost:8080 (or configured port)

**Health Check**: http://localhost:8080/actuator/health

---

## Project Structure

```
Knowledge-Tracker/
├── src/
│   ├── main/
│   │   ├── java/com/pragma/vc/tracker/
│   │   │   ├── domain/                    # Domain layer
│   │   │   ├── application/               # Application layer
│   │   │   ├── infrastructure/            # Infrastructure layer
│   │   │   └── KnowledgeTrackerApplication.java
│   │   └── resources/
│   │       ├── application.properties     # Base configuration
│   │       ├── application-dev.yml        # Development profile
│   │       ├── application-test.yml       # Test profile
│   │       └── application-prod.yml       # Production profile
│   └── test/
│       └── java/com/pragma/vc/tracker/    # Test classes
├── build.gradle                           # Gradle build configuration
├── settings.gradle                        # Gradle project settings
├── .env.example                           # Environment variables template
├── .gitignore                             # Git ignore rules
├── CLAUDE.md                              # Project guidelines for Claude Code
└── README.md                              # This file
```

---

## Development Guidelines

### Adding New Features

1. **Start with Domain Layer**
   - Define entities, value objects, and aggregates in `domain/model/`
   - Use pure Java - no framework annotations
   - Define repository interfaces (ports) in `domain/repository/`

2. **Add Application Layer**
   - Create use cases in `application/usecase/`
   - Use case classes should depend only on domain interfaces
   - Register use cases as Spring beans in `infrastructure/config/`

3. **Implement Infrastructure Adapters**
   - REST controllers in `infrastructure/adapter/web/`
   - JPA entities and repositories in `infrastructure/adapter/persistence/`
   - Use mappers to convert between layers

### Testing Strategy
- **Unit Tests** - Test domain logic with pure Java tests (no Spring context)
- **Integration Tests** - Test adapters with `@SpringBootTest` or slice tests
- **Use Case Tests** - Use `@MockBean` for ports when testing application layer

---

## Contributing

1. Follow the hexagonal architecture principles strictly
2. Maintain layer dependencies: Infrastructure → Application → Domain
3. Never import infrastructure code into domain/application layers
4. Use Lombok to reduce boilerplate code
5. Write tests for all new features
6. Follow the existing naming conventions and package structure

---

## License

Copyright © 2025 Pragma SA. All rights reserved.

---

## Contact & Support

For questions or support, please contact the Pragma SA development team.