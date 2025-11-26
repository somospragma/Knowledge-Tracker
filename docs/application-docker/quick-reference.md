# Docker Quick Reference - Pragma Knowledge Tracker

Quick commands for building and running the Knowledge Tracker Docker container.

## Build Commands

```bash
# Build using Gradle (Recommended)
./gradlew dockerBuild

# Build without cache
./gradlew dockerBuildNoCache

# Build with Docker CLI
docker build -t pragma/knowledge-tracker:latest .

# View Docker info
./gradlew dockerInfo
```

## Run Commands

```bash
# Quick run with Gradle
./gradlew dockerRun

# Run in detached mode (basic)
docker run -d \
  --name knowledge-tracker-app \
  -p 8080:8080 \
  pragma/knowledge-tracker:latest

# Run with full configuration
docker run -d \
  --name knowledge-tracker-app \
  --network knowledge-tracker_knowledge-tracker-network \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=dev \
  -e DB_HOST=postgres \
  -e DB_PORT=5432 \
  -e DB_NAME=knowledge_tracker_dev \
  -e DB_USERNAME=pragma_dev \
  -e DB_PASSWORD=pragma_dev_password \
  -e ES_HOST=elasticsearch \
  -e ES_PORT_APP=9200 \
  -e JAVA_OPTS="-Xms512m -Xmx1024m" \
  pragma/knowledge-tracker:latest
```

## Management Commands

```bash
# View running containers
docker ps

# View logs
docker logs -f knowledge-tracker-app

# Stop container
docker stop knowledge-tracker-app

# Start container
docker start knowledge-tracker-app

# Restart container
docker restart knowledge-tracker-app

# Remove container
docker rm knowledge-tracker-app

# Remove container (force)
docker rm -f knowledge-tracker-app
```

## Health & Status

```bash
# Check health status
curl http://localhost:8080/actuator/health

# View container stats
docker stats knowledge-tracker-app

# Inspect container
docker inspect knowledge-tracker-app

# Execute shell in container
docker exec -it knowledge-tracker-app sh

# Check container health
docker ps --filter name=knowledge-tracker-app
```

## Clean Up

```bash
# Stop and remove container
docker stop knowledge-tracker-app && docker rm knowledge-tracker-app

# Remove image (Gradle)
./gradlew dockerClean

# Remove image (Docker CLI)
docker rmi pragma/knowledge-tracker:latest
docker rmi pragma/knowledge-tracker:0.0.1-SNAPSHOT

# Clean all unused Docker resources
docker system prune -a
```

## Complete Workflow

```bash
# 1. Start infrastructure
./start-dev.sh

# 2. Build application image
./gradlew dockerBuild

# 3. Run application container
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

# 4. Check health
curl http://localhost:8080/actuator/health

# 5. View logs
docker logs -f knowledge-tracker-app
```

## Environment Variables

| Variable | Required | Default | Example |
|----------|----------|---------|---------|
| `SPRING_PROFILES_ACTIVE` | Yes | - | `dev`, `test`, `prod` |
| `DB_HOST` | Yes | - | `postgres`, `localhost` |
| `DB_PORT` | No | `5432` | `5432` |
| `DB_NAME` | Yes | - | `knowledge_tracker` |
| `DB_USERNAME` | Yes | - | `pragma_dev` |
| `DB_PASSWORD` | Yes | - | `password` |
| `ES_HOST` | Yes | - | `elasticsearch` |
| `ES_PORT_APP` | No | `9200` | `9200` |
| `JAVA_OPTS` | No | See Dockerfile | `-Xms512m -Xmx1024m` |

## Troubleshooting

```bash
# Port already in use
docker run -p 9090:8080 pragma/knowledge-tracker:latest

# Can't connect to database
docker network ls
docker network connect NETWORK_NAME knowledge-tracker-app

# View full logs
docker logs knowledge-tracker-app 2>&1 | less

# Debug network
docker exec knowledge-tracker-app ping postgres
docker exec knowledge-tracker-app nslookup postgres
```

## Service Endpoints

| Service | URL |
|---------|-----|
| Application | http://localhost:8080 |
| Health Check | http://localhost:8080/actuator/health |
| PostgreSQL | localhost:5432 |
| Elasticsearch | http://localhost:9200 |
| Logstash | http://localhost:9600 |
| Keycloak | http://localhost:8180 |

---

For complete documentation, see [Docker Deployment Guide](docker-deployment-guide.md)
