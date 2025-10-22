# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview
- **Project Name:** Pragma Knowledge Tracking System (Vigilancia)
- **Description:** A system to track and analyze technical knowledge that Pragma SA employees (Pragmatics) apply to client projects
- **Type:** Java Spring Boot Application
- **Version:** 0.0.1-SNAPSHOT
- **Spring Boot Version:** 3.5.6
- **Java Version:** Java 21
- **Build Tool:** Gradle 8.x with Gradle Wrapper
- **Database:** PostgreSQL 16 (production), H2 (development)

---

## Common Commands

### Build & Run
```bash
./gradlew build            # Build the entire project
./gradlew clean build      # Clean and rebuild
./gradlew bootRun          # Run the Spring Boot application
./gradlew clean            # Clean build artifacts
```

### Testing
```bash
./gradlew test                                    # Run all tests
./gradlew test --tests ClassName                  # Run a specific test class
./gradlew test --tests ClassName.methodName       # Run a specific test method
./gradlew test --tests 'com.pragma.vc.tracker.*'  # Run tests in a package
./gradlew test --info                             # Run tests with detailed output
./gradlew test --rerun-tasks                      # Force re-run even if up-to-date
./gradlew check                                   # Run all checks (tests + quality)
```

### Gradle
```bash
./gradlew dependencies     # Show dependency tree
./gradlew tasks --all      # List all available tasks
./gradlew bootJar          # Build executable JAR
```

---

## Current Project Structure

```
Knowledge-Tracker/
├── src/
│   ├── main/
│   │   ├── java/com/pragma/vc/tracker/
│   │   │   └── KnowledgeTrackerApplication.java    # Spring Boot entry point
│   │   └── resources/
│   │       └── application.properties              # Spring configuration
│   └── test/
│       └── java/com/pragma/vc/tracker/
│           └── KnowledgeTrackerApplicationTests.java
├── build.gradle                                    # Gradle build configuration
├── settings.gradle                                 # Gradle project settings
├── gradlew                                         # Gradle wrapper (Unix)
└── gradlew.bat                                     # Gradle wrapper (Windows)
```

**Current State:** This is a newly initialized Spring Boot project with minimal implementation. The project is in the early stages of development.

---

## Target Architecture (Under Development)

The project is being built with the following architectural principles:

### Architectural Style
**Hexagonal Architecture (Ports & Adapters) with Domain-Driven Design**

### Layer Structure (To Be Implemented)
```
src/main/java/com/pragma/vc/tracker/
├── domain/                          # Domain Layer (Pure Java, no frameworks)
│   ├── model/                       # Entities, Value Objects, Aggregates
│   ├── service/                     # Domain Services
│   └── repository/                  # Repository interfaces (ports)
├── application/                     # Application Layer (Use Cases)
│   ├── usecase/                     # Application services, orchestration
│   └── port/                        # Input/Output ports for use cases
└── infrastructure/                  # Infrastructure Layer (Framework code)
    ├── adapter/
    │   ├── web/                     # REST Controllers (input adapters)
    │   ├── persistence/             # JPA Repositories (output adapters)
    │   └── messaging/               # Kafka producers/consumers
    └── config/                      # Spring configuration classes
```

### Domain Model (Planned)

**Bounded Contexts:**
1. **Knowledge Application (Core)** - Track what knowledge Pragmatics apply to projects
2. **Project Management** - Manage client accounts and projects
3. **People Management** - Manage Pragma SA employees (Pragmatics)
4. **Knowledge Catalog** - Define technical knowledge categories

**Key Aggregates:**
- `Account` - Client organization
- `Project` - Client project with assigned Pragmatics
- `Pragmatic` - Pragma SA employee
- `Knowledge` - Technical skill/knowledge (platform, language, framework, tool, technique)
- `AppliedKnowledge` (Core) - Assignment of knowledge to project by pragmatic at a proficiency level

**Value Objects:**
- `ProjectStatus` - Active, Inactive, Completed
- `PragmaticStatus` - Active, Inactive, OnLeave
- `KnowledgeLevel` - Beginner, Intermediate, Advanced, Expert
- `KnowledgeCategory` - Platform, Language, Framework, Tool, Technique
- `Email` - Valid email address
- `DateRange` - Start and end dates

### Layer Dependencies Rule
```
Domain Layer (Pure Java)
    ↑
    │ depends on
    │
Application Layer (Use Cases)
    ↑
    │ depends on
    │
Infrastructure Layer (Spring, JPA, REST)
```

**Critical Implementation Rules:**
- ✅ Domain layer: NO Spring annotations, NO JPA, NO framework dependencies
- ✅ Application layer: Depends ONLY on domain interfaces (ports)
- ✅ Infrastructure layer: Implements ports, uses Spring framework
- ❌ NEVER import infrastructure code into domain/application layers
- ❌ NEVER use `@Entity` in domain layer - use it only in infrastructure/persistence adapters

**Correct Pattern Example:**
```
// Domain Layer (domain/model/Account.java)
public class Account {
    private AccountId id;
    private String name;
    // Pure business logic, no annotations
}

// Domain Layer (domain/repository/AccountRepository.java)
public interface AccountRepository {  // Port
    Account save(Account account);
    Optional<Account> findById(AccountId id);
}

// Infrastructure Layer (infrastructure/adapter/persistence/JpaAccountEntity.java)
@Entity
@Table(name = "accounts")
public class JpaAccountEntity {
    @Id private String id;
    @Column private String name;
    // JPA mapping
}

// Infrastructure Layer (infrastructure/adapter/persistence/JpaAccountRepositoryAdapter.java)
@Repository
public class JpaAccountRepositoryAdapter implements AccountRepository {
    // Implements port, converts between domain and JPA entities
}
```

---

## Key Dependencies

**Spring Boot Starters:**
- `spring-boot-starter-web` - REST API support
- `spring-boot-starter-data-jpa` - Data persistence
- `spring-boot-starter-actuator` - Application monitoring
- `spring-boot-devtools` - Development hot reloading

**Database:**
- `h2` - In-memory database for development/testing
- `postgresql` - Production database

**Development:**
- `lombok` - Reduce boilerplate code (@Getter, @Setter, @Builder, etc.)

**Testing:**
- `spring-boot-starter-test` - Includes JUnit 5, Mockito, AssertJ
- `junit-platform-launcher` - JUnit 5 test runner

---

## Configuration

**Main Configuration:** `src/main/resources/application.properties`
- Application name: `Knowledge-Tracker`
- Default configuration is minimal; profiles should be added as needed

**Development Profile (`dev`)** - To be configured
- Use H2 in-memory database
- Enable Spring DevTools
- Console logging with DEBUG level

**Production Profile (`prod`)** - To be configured
- Use PostgreSQL database
- Disable DevTools
- JSON logging for production

**To run with specific profile:**
```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
./gradlew bootRun --args='--spring.profiles.active=prod'
```

**Profile-specific files to create:**
- `src/main/resources/application-dev.properties` - Development settings
- `src/main/resources/application-prod.properties` - Production settings

---

## Development Guidelines

### When Adding New Features

1. **Start with the Domain Layer**
   - Define entities, value objects, and aggregates in `domain/model/`
   - Use pure Java - no framework annotations
   - Define repository interfaces (ports) in `domain/repository/`

2. **Add Application Layer**
   - Create use cases in `application/usecase/`
   - Use case classes should depend only on domain interfaces

3. **Implement Infrastructure Adapters**
   - REST controllers in `infrastructure/adapter/web/`
   - JPA entities and repositories in `infrastructure/adapter/persistence/`
   - Use mappers to convert between layers

### Testing Strategy
- **Unit Tests:** Test domain logic with pure Java tests (no Spring context)
- **Integration Tests:** Test adapters with `@SpringBootTest` or slice tests
- **Use case:** `@MockBean` for ports when testing application layer

### Lombok Usage
Use Lombok to reduce boilerplate:
```java
@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Account {
    private AccountId id;
    private String name;
}
```

---

## Business Context

**Business Goals:**
1. Track technical knowledge applied across all Pragma SA projects
2. Enable better resource allocation based on Pragmatic expertise
3. Identify skills gaps and training needs
4. Provide clients visibility into technical capabilities
5. Support strategic decisions for technology investments and hiring

**Target Users:**
- Pragma SA management (resource allocation)
- Project managers (skills visibility)
- Clients (transparency)
- HR and training teams (skills development)

---

## Next Steps for Implementation

When starting feature development, follow this order:

1. **Domain Model** - Define entities and aggregates with business rules
2. **Repository Ports** - Define interfaces in domain layer
3. **Use Cases** - Implement application services
4. **REST Controllers** - Expose APIs
5. **JPA Adapters** - Implement persistence
6. **Tests** - Add comprehensive test coverage

Always maintain the dependency rule: Infrastructure → Application → Domain (never reversed).