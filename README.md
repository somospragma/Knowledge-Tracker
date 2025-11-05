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
- **PostgreSQL** - 16 (production database)
- **ELK Stack** - Full Elastic Stack for search and analytics
  - **Elasticsearch** - 8.11.0 (search engine and analytics)
  - **Logstash** - 8.11.0 (data processing and PostgreSQL sync)
  - **Kibana** - 8.11.0 (visualization and dashboards)
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

## Quick Start

Get up and running in 3 steps:

```bash
# 1. Clone and navigate to the project
git clone <repository-url>
cd Knowledge-Tracker

# 2. Start ELK Stack (PostgreSQL + Elasticsearch + Logstash) with Docker
chmod +x start-dev.sh
./start-dev.sh

# 3. Run the application
./gradlew bootRun --args='--spring.profiles.active=dev'
```

The application will be available at http://localhost:8080
- **PostgreSQL**: localhost:5432
- **Elasticsearch**: http://localhost:9200
- **Logstash**: http://localhost:9600 (monitoring API)

---

## Build & Run

### Prerequisites
- Java 21 or higher
- Docker and Docker Compose (recommended for local development)
- Git

**Note:** PostgreSQL 16 is required but can be easily started using Docker (see Environment Setup below). Manual PostgreSQL installation is optional.

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

   # Edit .env with your configuration (optional - defaults work for local development)
   nano .env
   ```

3. **Start Development Services with Docker (Recommended)**

   The easiest way to get started is using Docker Compose with the provided `start-dev.sh` script:

   ```bash
   # Make the script executable (first time only)
   chmod +x start-dev.sh

   # Start PostgreSQL and Elasticsearch
   ./start-dev.sh

   # Start with pgAdmin (PostgreSQL management UI)
   ./start-dev.sh --with-pgadmin

   # Start with Kibana (Elasticsearch visualization)
   ./start-dev.sh --with-kibana

   # Start with all optional services
   ./start-dev.sh --with-all

   # Rebuild containers before starting
   ./start-dev.sh --rebuild

   # View help
   ./start-dev.sh --help
   ```

   **What the script does:**
   - Checks if Docker is running
   - Creates `.env` file from `.env.example` if not present
   - Starts the full ELK stack in Docker containers:
     - PostgreSQL 16 (primary database)
     - Elasticsearch 8.11.0 (search engine)
     - Logstash 8.11.0 (data sync and processing)
   - Waits for all services to be ready and healthy
   - Optionally starts pgAdmin and/or Kibana
   - Displays connection details and monitoring endpoints

   **PostgreSQL Connection Details** (default values):
   - Host: `localhost`
   - Port: `5432`
   - Database: `knowledge_tracker_dev`
   - Username: `pragma_dev`
   - Password: `pragma_dev_password`

   **Elasticsearch Connection Details** (default values):
   - URL: http://localhost:9200
   - Health Check: http://localhost:9200/_cluster/health
   - Security: Disabled (development mode)

   **Logstash Connection Details** (default values):
   - Monitoring API: http://localhost:9600
   - Node Stats: http://localhost:9600/_node/stats
   - Pipeline Stats: http://localhost:9600/_node/stats/pipelines?pretty
   - **Pipelines**: 5 automated sync pipelines running every 30-60 seconds
     - `accounts-sync` - Syncs client accounts with territory info
     - `projects-sync` - Syncs projects with account and staffing data
     - `pragmatics-sync` - Syncs employees with chapter and knowledge data
     - `knowledge-sync` - Syncs knowledge catalog with usage statistics
     - `applied-knowledge-sync` - Syncs knowledge applications (CORE)

   **pgAdmin Access** (when using `--with-pgadmin`):
   - URL: http://localhost:5050
   - Email: `admin@pragma.com`
   - Password: `admin`

   **Kibana Access** (when using `--with-kibana`):
   - URL: http://localhost:5601
   - Note: May take 1-2 minutes to fully initialize

   **Useful Docker Commands:**
   ```bash
   # Stop containers
   docker-compose down

   # View service logs
   docker-compose logs -f postgres elasticsearch logstash

   # View Logstash logs only
   docker-compose logs -f logstash

   # View all container logs
   docker-compose logs -f

   # Stop and remove volumes (WARNING: deletes all data)
   docker-compose down -v

   # Connect to PostgreSQL directly
   docker exec -it knowledge-tracker-postgres-dev psql -U pragma_dev -d knowledge_tracker_dev

   # Check Elasticsearch health
   curl http://localhost:9200/_cluster/health?pretty

   # Monitor Logstash pipelines
   curl http://localhost:9600/_node/stats/pipelines?pretty

   # Check Logstash node info
   curl http://localhost:9600/_node?pretty

   # List Elasticsearch indices created by Logstash
   curl http://localhost:9200/_cat/indices?v

   # Search data in Elasticsearch (example: accounts)
   curl http://localhost:9200/accounts-*/_search?pretty

   # View Logstash pipeline configuration
   docker exec -it knowledge-tracker-logstash-dev ls -la /usr/share/logstash/pipeline/
   ```

4. **ELK Stack Data Flow**

   The Logstash pipelines automatically sync data from PostgreSQL to Elasticsearch:

   ```
   PostgreSQL (Source of Truth)
        ↓
   [Logstash JDBC Input]
        ↓ (30-60 second intervals)
   [Logstash Filters & Enrichment]
        ├─ Parse JSON attributes
        ├─ Calculate derived fields
        ├─ Join related entities
        └─ Add metadata
        ↓
   [Elasticsearch Indices]
        ├─ accounts-YYYY.MM
        ├─ projects-YYYY.MM
        ├─ pragmatics-YYYY.MM
        ├─ knowledge
        └─ applied-knowledge-YYYY.MM
        ↓
   [Kibana Dashboards] (optional)
        └─ Analytics & Visualizations
   ```

   **Benefits:**
   - Near real-time search capabilities
   - Complex analytics without impacting PostgreSQL
   - Full-text search on all entities
   - Pre-aggregated statistics (e.g., knowledge usage, project staffing)
   - Time-series data with monthly indices
   - Easy data exploration via Kibana

5. **Alternative: Manual Setup**

   If you prefer to install services manually instead of using Docker:

   **PostgreSQL:**
   ```sql
   CREATE DATABASE knowledge_tracker_dev;
   CREATE DATABASE knowledge_tracker_test;
   CREATE DATABASE knowledge_tracker;  -- production
   ```

   **Elasticsearch:**
   - Download and install Elasticsearch 8.11.0 from https://www.elastic.co/downloads/elasticsearch
   - Disable security for development: Add `xpack.security.enabled: false` to `elasticsearch.yml`
   - Start Elasticsearch service

   Then update your `.env` file with your service credentials and connection details.

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

**Development Profile (dev):**

| Variable | Default | Description |
|----------|---------|-------------|
| `DB_HOST` | localhost | PostgreSQL host |
| `DB_PORT` | 5432 | PostgreSQL port |
| `DB_NAME` | knowledge_tracker_dev | PostgreSQL database name |
| `DB_USERNAME` | pragma_dev | PostgreSQL username |
| `DB_PASSWORD` | pragma_dev_password | PostgreSQL password |
| `ES_HOST` | localhost | Elasticsearch host |
| `ES_PORT_APP` | 9200 | Elasticsearch port |
| `ES_USERNAME` | elastic | Elasticsearch username |
| `ES_PASSWORD_APP` | elasticsearch | Elasticsearch password |
| `SERVER_PORT` | 8080 | Application server port |

**Docker Compose Variables:**

| Variable | Default | Description |
|----------|---------|-------------|
| `POSTGRES_PORT` | 5432 | PostgreSQL container port |
| `ES_PORT` | 9200 | Elasticsearch container port |
| `ES_HEAP_SIZE` | 512m | Elasticsearch JVM heap size |
| `LOGSTASH_PORT` | 5000 | Logstash input port |
| `LOGSTASH_HEAP_SIZE` | 256m | Logstash JVM heap size |
| `PGADMIN_PORT` | 5050 | pgAdmin port |
| `KIBANA_PORT` | 5601 | Kibana port |

See `.env.example` for a complete list of available environment variables for all profiles (dev, test, prod).

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
├── logstash/
│   ├── config/
│   │   ├── logstash.yml                   # Logstash main configuration
│   │   └── pipelines.yml                  # Pipeline definitions
│   └── pipeline/
│       ├── accounts-pipeline.conf         # Accounts sync pipeline
│       ├── projects-pipeline.conf         # Projects sync pipeline
│       ├── pragmatics-pipeline.conf       # Pragmatics sync pipeline
│       ├── knowledge-pipeline.conf        # Knowledge catalog sync pipeline
│       └── applied-knowledge-pipeline.conf # Applied knowledge sync pipeline
├── docker-compose.yml                     # Docker services configuration
├── start-dev.sh                           # Development environment startup script
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