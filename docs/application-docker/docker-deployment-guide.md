# Docker Deployment Guide - Pragma Knowledge Tracker

This guide provides comprehensive instructions for building, creating, and running the Knowledge Tracker application as a Docker container.

## Table of Contents

- [Overview](#overview)
- [Prerequisites](#prerequisites)
- [Docker Image Architecture](#docker-image-architecture)
- [Building the Docker Image](#building-the-docker-image)
  - [Using Gradle Tasks (Recommended)](#using-gradle-tasks-recommended)
  - [Using Docker CLI](#using-docker-cli)
  - [Build Options](#build-options)
- [Running the Docker Container](#running-the-docker-container)
  - [Quick Start](#quick-start)
  - [Standalone Container](#standalone-container)
  - [With Docker Compose](#with-docker-compose)
- [Configuration](#configuration)
  - [Environment Variables](#environment-variables)
  - [Network Configuration](#network-configuration)
  - [Volume Mounts](#volume-mounts)
- [Managing the Container](#managing-the-container)
  - [Container Lifecycle](#container-lifecycle)
  - [Viewing Logs](#viewing-logs)
  - [Health Checks](#health-checks)
- [Troubleshooting](#troubleshooting)
- [Production Deployment](#production-deployment)
- [Docker Commands Reference](#docker-commands-reference)

---

## Overview

The Knowledge Tracker application is containerized using Docker with a multi-stage build process that creates an optimized, production-ready image.

**Image Specifications:**
- **Base Image**: Eclipse Temurin JRE 21 (Alpine Linux)
- **Image Size**: ~360 MB
- **Security**: Runs as non-root user
- **Health Checks**: Built-in Spring Boot Actuator health endpoint
- **JVM Optimization**: Configured for production workloads

---

## Prerequisites

Before you begin, ensure you have the following installed:

- **Docker**: Version 20.10 or later
- **Docker Compose**: Version 2.0 or later (optional, for multi-container setup)
- **Java 21**: Required for building the application JAR
- **Gradle**: 8.x (included via Gradle Wrapper)

**Verify Installation:**

```bash
# Check Docker version
docker --version

# Check Docker Compose version
docker-compose --version

# Check Java version
java --version
```

---

## Docker Image Architecture

The Dockerfile uses a **multi-stage build** approach:

### Stage 1: Builder
- **Base**: `gradle:8.11.1-jdk21-alpine`
- **Purpose**: Build the application JAR
- **Steps**:
  1. Copy Gradle wrapper and build files
  2. Download dependencies (cached layer)
  3. Copy source code
  4. Build the JAR using `./gradlew bootJar`

### Stage 2: Runtime
- **Base**: `eclipse-temurin:21-jre-alpine`
- **Purpose**: Run the application
- **Features**:
  - Creates non-root user (`spring`)
  - Minimal runtime dependencies
  - Health check configuration
  - Optimized JVM settings

**Dockerfile Location**: `./Dockerfile` (project root)

---

## Building the Docker Image

### Using Gradle Tasks (Recommended)

The project includes custom Gradle tasks for Docker operations:

#### 1. View Available Docker Tasks

```bash
./gradlew dockerInfo
```

**Output:**
```
Docker Image Information:
=========================
Image Name: pragma/knowledge-tracker
Version: 0.0.1-SNAPSHOT
Full Name: pragma/knowledge-tracker:0.0.1-SNAPSHOT
Latest Tag: pragma/knowledge-tracker:latest

Available Docker Tasks:
  ./gradlew dockerBuild       - Build Docker image
  ./gradlew dockerBuildNoCache - Build without cache
  ./gradlew dockerRun         - Run container locally
  ./gradlew dockerStop        - Stop running container
  ./gradlew dockerPush        - Push to registry
  ./gradlew dockerClean       - Remove Docker image
  ./gradlew dockerInfo        - Show this information
```

#### 2. Build the Docker Image

```bash
./gradlew dockerBuild
```

**What happens:**
1. Compiles the application
2. Builds the JAR file (`bootJar` task)
3. Creates Docker image with two tags:
   - `pragma/knowledge-tracker:0.0.1-SNAPSHOT`
   - `pragma/knowledge-tracker:latest`

**Expected Output:**
```
> Task :bootJar
> Task :dockerBuild
Docker image built successfully:
  - pragma/knowledge-tracker:0.0.1-SNAPSHOT
  - pragma/knowledge-tracker:latest

BUILD SUCCESSFUL
```

#### 3. Build Without Cache (Clean Build)

```bash
./gradlew dockerBuildNoCache
```

Use this when:
- Dependencies have changed
- You need a completely fresh build
- Troubleshooting build issues

### Using Docker CLI

You can also build the image directly with Docker:

```bash
# Build with latest tag
docker build -t pragma/knowledge-tracker:latest .

# Build with specific version
docker build -t pragma/knowledge-tracker:0.0.1-SNAPSHOT .

# Build with custom tag
docker build -t my-registry.com/pragma/knowledge-tracker:1.0.0 .
```

### Build Options

#### Build Arguments

The Dockerfile accepts build arguments (though none are currently defined):

```bash
docker build \
  --build-arg JAR_FILE=build/libs/custom.jar \
  -t pragma/knowledge-tracker:latest .
```

#### Build for Different Platforms

```bash
# Build for multiple platforms (ARM64 and AMD64)
docker buildx build \
  --platform linux/amd64,linux/arm64 \
  -t pragma/knowledge-tracker:latest .
```

### Verify the Image

After building, verify the image was created:

```bash
# List images
docker images | grep pragma/knowledge-tracker

# Inspect the image
docker inspect pragma/knowledge-tracker:latest

# Check image size
docker images pragma/knowledge-tracker --format "table {{.Repository}}:{{.Tag}}\t{{.Size}}"
```

---

## Running the Docker Container

### Quick Start

The fastest way to run the container:

```bash
./gradlew dockerRun
```

This command:
- Builds the image if it doesn't exist
- Runs the container with default settings
- Maps port 8080 to host
- Uses development profile
- Runs in foreground (interactive mode)

**To stop**: Press `Ctrl+C`

### Standalone Container

Run the container independently with custom configuration:

#### Basic Run

```bash
docker run -d \
  --name knowledge-tracker-app \
  -p 8080:8080 \
  pragma/knowledge-tracker:latest
```

#### Run with Environment Variables

```bash
docker run -d \
  --name knowledge-tracker-app \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_HOST=postgres.example.com \
  -e DB_PORT=5432 \
  -e DB_NAME=knowledge_tracker \
  -e DB_USERNAME=app_user \
  -e DB_PASSWORD=secret_password \
  -e ES_HOST=elasticsearch.example.com \
  -e ES_PORT_APP=9200 \
  -e JAVA_OPTS="-Xms512m -Xmx1024m" \
  pragma/knowledge-tracker:latest
```

#### Run with External Network

```bash
docker run -d \
  --name knowledge-tracker-app \
  --network my-custom-network \
  -p 8080:8080 \
  -e DB_HOST=postgres \
  -e ES_HOST=elasticsearch \
  pragma/knowledge-tracker:latest
```

### With Docker Compose

#### Option 1: Add to Existing docker-compose.yml

Add this service definition to your `docker-compose.yml`:

```yaml
services:
  app:
    image: pragma/knowledge-tracker:latest
    container_name: knowledge-tracker-app
    ports:
      - "8080:8080"
    environment:
      SPRING_PROFILES_ACTIVE: prod
      DB_HOST: postgres
      DB_PORT: 5432
      DB_NAME: ${DB_NAME:-knowledge_tracker_dev}
      DB_USERNAME: ${DB_USERNAME:-pragma_dev}
      DB_PASSWORD: ${DB_PASSWORD:-pragma_dev_password}
      ES_HOST: elasticsearch
      ES_PORT_APP: 9200
      JAVA_OPTS: "-Xms512m -Xmx1024m"
    depends_on:
      postgres:
        condition: service_healthy
      elasticsearch:
        condition: service_healthy
    healthcheck:
      test: ["CMD", "wget", "--no-verbose", "--tries=1", "--spider", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 3s
      retries: 3
      start_period: 60s
    networks:
      - knowledge-tracker-network
    restart: unless-stopped

networks:
  knowledge-tracker-network:
    driver: bridge

volumes:
  postgres_data:
  elasticsearch_data:
  logstash_data:
```

#### Option 2: Start All Services

```bash
# Start all services including the application
docker-compose up -d

# View logs
docker-compose logs -f app

# Stop all services
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

#### Option 3: Development with Hot Reload

For development with the existing infrastructure:

```bash
# 1. Start infrastructure services
./start-dev.sh

# 2. Run the application container
docker run -d \
  --name knowledge-tracker-app \
  --network knowledge-tracker_knowledge-tracker-network \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=dev \
  -e DB_HOST=postgres \
  -e DB_NAME=knowledge_tracker_dev \
  -e DB_USERNAME=pragma_dev \
  -e DB_PASSWORD=pragma_dev_password \
  -e ES_HOST=elasticsearch \
  pragma/knowledge-tracker:latest
```

---

## Configuration

### Environment Variables

The application accepts the following environment variables:

#### Required Variables

| Variable | Description | Example |
|----------|-------------|---------|
| `SPRING_PROFILES_ACTIVE` | Spring Boot profile | `dev`, `test`, `prod` |
| `DB_HOST` | PostgreSQL host | `postgres`, `localhost` |
| `DB_NAME` | Database name | `knowledge_tracker` |
| `DB_USERNAME` | Database username | `pragma_dev` |
| `DB_PASSWORD` | Database password | `secure_password` |
| `ES_HOST` | Elasticsearch host | `elasticsearch` |

#### Optional Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `DB_PORT` | `5432` | PostgreSQL port |
| `ES_PORT_APP` | `9200` | Elasticsearch port |
| `SERVER_PORT` | `8080` | Application port |
| `JAVA_OPTS` | `-Xms256m -Xmx512m -XX:+UseG1GC` | JVM options |

#### JVM Configuration

Customize JVM settings for different environments:

**Development:**
```bash
-e JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"
```

**Production (Small):**
```bash
-e JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
```

**Production (Large):**
```bash
-e JAVA_OPTS="-Xms1g -Xmx2g -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+UseStringDeduplication"
```

### Network Configuration

#### Using Docker Networks

```bash
# Create a custom network
docker network create knowledge-tracker-net

# Run container on custom network
docker run -d \
  --name knowledge-tracker-app \
  --network knowledge-tracker-net \
  -p 8080:8080 \
  pragma/knowledge-tracker:latest

# Connect to existing network
docker network connect knowledge-tracker_knowledge-tracker-network knowledge-tracker-app
```

#### Port Mapping

```bash
# Map to different host port
docker run -d -p 9090:8080 pragma/knowledge-tracker:latest

# Bind to specific interface
docker run -d -p 127.0.0.1:8080:8080 pragma/knowledge-tracker:latest

# Expose multiple ports
docker run -d \
  -p 8080:8080 \
  -p 8081:8081 \
  pragma/knowledge-tracker:latest
```

### Volume Mounts

Mount external configuration or logs:

```bash
docker run -d \
  --name knowledge-tracker-app \
  -p 8080:8080 \
  -v $(pwd)/config:/config \
  -v $(pwd)/logs:/app/logs \
  -e SPRING_CONFIG_LOCATION=file:/config/application.yml \
  pragma/knowledge-tracker:latest
```

---

## Managing the Container

### Container Lifecycle

```bash
# Start the container
docker start knowledge-tracker-app

# Stop the container (graceful shutdown)
docker stop knowledge-tracker-app

# Stop with timeout
docker stop -t 30 knowledge-tracker-app

# Restart the container
docker restart knowledge-tracker-app

# Pause the container
docker pause knowledge-tracker-app

# Unpause the container
docker unpause knowledge-tracker-app

# Remove the container
docker rm knowledge-tracker-app

# Force remove running container
docker rm -f knowledge-tracker-app
```

### Viewing Logs

```bash
# View all logs
docker logs knowledge-tracker-app

# Follow logs (live tail)
docker logs -f knowledge-tracker-app

# View last 100 lines
docker logs --tail 100 knowledge-tracker-app

# View logs with timestamps
docker logs -t knowledge-tracker-app

# View logs since specific time
docker logs --since 2024-01-01T00:00:00 knowledge-tracker-app

# View logs between times
docker logs --since 1h knowledge-tracker-app
```

### Health Checks

The container includes built-in health checks:

```bash
# Check container health status
docker inspect knowledge-tracker-app | grep -A 10 "Health"

# View health check logs
docker inspect knowledge-tracker-app \
  --format='{{json .State.Health}}' | python3 -m json.tool

# Manual health check
docker exec knowledge-tracker-app \
  wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health
```

**Health Check Endpoints:**

```bash
# Application health
curl http://localhost:8080/actuator/health

# Detailed health (with credentials if security enabled)
curl http://localhost:8080/actuator/health/details
```

### Container Statistics

```bash
# View real-time stats
docker stats knowledge-tracker-app

# View resource usage
docker inspect knowledge-tracker-app \
  --format='{{.HostConfig.Memory}}'

# View disk usage
docker system df
```

### Executing Commands in Container

```bash
# Open shell
docker exec -it knowledge-tracker-app sh

# Run specific command
docker exec knowledge-tracker-app ps aux

# Check Java version
docker exec knowledge-tracker-app java --version

# View environment variables
docker exec knowledge-tracker-app env

# Check network connectivity
docker exec knowledge-tracker-app ping -c 3 postgres
```

---

## Troubleshooting

### Common Issues

#### 1. Container Won't Start

**Symptoms:**
```bash
docker ps -a
# Shows container with status "Exited"
```

**Solution:**
```bash
# Check logs for errors
docker logs knowledge-tracker-app

# Common causes:
# - Missing environment variables
# - Database connection issues
# - Port already in use
```

#### 2. Port Already in Use

**Error:**
```
Error starting userland proxy: listen tcp 0.0.0.0:8080: bind: address already in use
```

**Solution:**
```bash
# Find process using port 8080
lsof -i :8080

# Stop the process or use different port
docker run -p 9090:8080 pragma/knowledge-tracker:latest
```

#### 3. Database Connection Failed

**Symptoms:**
```
HikariPool-1 - Exception during pool initialization
Connection refused
```

**Solutions:**

```bash
# Verify database is running
docker ps | grep postgres

# Check network connectivity
docker exec knowledge-tracker-app ping postgres

# Verify environment variables
docker exec knowledge-tracker-app env | grep DB_

# Check database logs
docker logs knowledge-tracker-postgres-dev
```

#### 4. Out of Memory

**Symptoms:**
```
java.lang.OutOfMemoryError: Java heap space
```

**Solution:**
```bash
# Increase heap size
docker run -e JAVA_OPTS="-Xms1g -Xmx2g" pragma/knowledge-tracker:latest

# Or recreate container with more memory
docker run --memory=2g pragma/knowledge-tracker:latest
```

#### 5. Image Build Fails

**Solutions:**

```bash
# Clean build
./gradlew clean dockerBuildNoCache

# Check Docker disk space
docker system df

# Clean up unused images
docker system prune -a

# Build with verbose output
docker build --progress=plain -t pragma/knowledge-tracker:latest .
```

### Debug Mode

Run the container in debug mode:

```bash
docker run -it --rm \
  -p 8080:8080 \
  -p 5005:5005 \
  -e JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005" \
  pragma/knowledge-tracker:latest
```

Connect your IDE debugger to `localhost:5005`.

---

## Production Deployment

### Best Practices

#### 1. Use Specific Version Tags

```bash
# Don't use in production
docker run pragma/knowledge-tracker:latest

# Use specific version
docker run pragma/knowledge-tracker:0.0.1-SNAPSHOT

# Or commit hash
docker run pragma/knowledge-tracker:abc123
```

#### 2. Resource Limits

```bash
docker run -d \
  --name knowledge-tracker-app \
  --memory="2g" \
  --memory-swap="2g" \
  --cpus="2.0" \
  --restart=unless-stopped \
  pragma/knowledge-tracker:latest
```

#### 3. Security Hardening

```bash
docker run -d \
  --name knowledge-tracker-app \
  --read-only \
  --tmpfs /tmp \
  --security-opt=no-new-privileges:true \
  --cap-drop=ALL \
  --cap-add=NET_BIND_SERVICE \
  pragma/knowledge-tracker:latest
```

#### 4. Logging

```bash
docker run -d \
  --name knowledge-tracker-app \
  --log-driver json-file \
  --log-opt max-size=10m \
  --log-opt max-file=3 \
  pragma/knowledge-tracker:latest
```

#### 5. Health Checks and Restart Policies

```bash
docker run -d \
  --name knowledge-tracker-app \
  --restart=unless-stopped \
  --health-cmd='wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1' \
  --health-interval=30s \
  --health-timeout=3s \
  --health-retries=3 \
  --health-start-period=60s \
  pragma/knowledge-tracker:latest
```

### Container Registry

#### Push to Docker Hub

```bash
# Login
docker login

# Tag for Docker Hub
docker tag pragma/knowledge-tracker:latest username/knowledge-tracker:0.0.1

# Push
docker push username/knowledge-tracker:0.0.1
```

#### Push to Private Registry

```bash
# Tag for private registry
docker tag pragma/knowledge-tracker:latest registry.company.com/pragma/knowledge-tracker:0.0.1

# Login to registry
docker login registry.company.com

# Push
docker push registry.company.com/pragma/knowledge-tracker:0.0.1

# Or use Gradle task
./gradlew dockerPush
```

### Kubernetes Deployment

Example Kubernetes deployment manifest:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: knowledge-tracker
spec:
  replicas: 3
  selector:
    matchLabels:
      app: knowledge-tracker
  template:
    metadata:
      labels:
        app: knowledge-tracker
    spec:
      containers:
      - name: knowledge-tracker
        image: pragma/knowledge-tracker:0.0.1-SNAPSHOT
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
        - name: DB_HOST
          value: "postgres-service"
        - name: DB_USERNAME
          valueFrom:
            secretKeyRef:
              name: db-credentials
              key: username
        - name: DB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: db-credentials
              key: password
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 30
        readinessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        resources:
          requests:
            memory: "512Mi"
            cpu: "500m"
          limits:
            memory: "1Gi"
            cpu: "1000m"
```

---

## Docker Commands Reference

### Quick Reference

```bash
# BUILD
./gradlew dockerBuild              # Build image using Gradle
./gradlew dockerBuildNoCache       # Build without cache
docker build -t NAME:TAG .         # Build with Docker CLI

# RUN
./gradlew dockerRun                # Run with Gradle
docker run -d -p 8080:8080 IMAGE   # Run detached
docker run -it IMAGE               # Run interactive

# MANAGE
docker ps                          # List running containers
docker ps -a                       # List all containers
docker stop CONTAINER              # Stop container
docker start CONTAINER             # Start container
docker restart CONTAINER           # Restart container
docker rm CONTAINER                # Remove container

# LOGS & DEBUG
docker logs CONTAINER              # View logs
docker logs -f CONTAINER           # Follow logs
docker exec -it CONTAINER sh       # Open shell

# CLEAN UP
./gradlew dockerClean              # Remove image using Gradle
docker rmi IMAGE                   # Remove image
docker system prune -a             # Remove all unused images

# INSPECT
docker inspect CONTAINER           # View details
docker stats CONTAINER             # View stats
docker images                      # List images
```

### Environment-Specific Commands

**Development:**
```bash
docker run -d -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=dev \
  pragma/knowledge-tracker:latest
```

**Testing:**
```bash
docker run -d -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=test \
  pragma/knowledge-tracker:latest
```

**Production:**
```bash
docker run -d -p 8080:8080 \
  --restart=unless-stopped \
  --memory="1g" \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_HOST=prod-db \
  pragma/knowledge-tracker:latest
```

---

## Additional Resources

- [Dockerfile Reference](https://docs.docker.com/engine/reference/builder/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Spring Boot Docker Guide](https://spring.io/guides/gs/spring-boot-docker/)
- [Docker Security Best Practices](https://docs.docker.com/develop/security-best-practices/)

---

**Last Updated**: November 2025
**Version**: 1.0.0
**Maintained by**: Pragma SA Development Team
