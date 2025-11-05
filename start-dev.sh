#!/bin/bash

# Script to start the Knowledge Tracker development environment
# This script will start PostgreSQL in Docker and optionally pgAdmin

set -e  # Exit on error

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}================================================${NC}"
echo -e "${GREEN}Knowledge Tracker - Dev Environment Setup${NC}"
echo -e "${GREEN}================================================${NC}"

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo -e "${RED}Error: Docker is not running. Please start Docker and try again.${NC}"
    exit 1
fi

# Check if .env file exists
if [ ! -f .env ]; then
    echo -e "${YELLOW}Warning: .env file not found. Creating from .env.example...${NC}"
    if [ -f .env.example ]; then
        cp .env.example .env
        echo -e "${GREEN}.env file created. Please review and update if needed.${NC}"
    else
        echo -e "${RED}Error: .env.example not found. Please create .env file manually.${NC}"
        exit 1
    fi
fi

# Load environment variables
if [ -f .env ]; then
    export $(cat .env | grep -v '^#' | xargs)
fi

# Parse command line arguments
WITH_PGADMIN=false
WITH_KIBANA=false
REBUILD=false

while [[ $# -gt 0 ]]; do
    case $1 in
        --with-pgadmin)
            WITH_PGADMIN=true
            shift
            ;;
        --with-kibana)
            WITH_KIBANA=true
            shift
            ;;
        --with-all)
            WITH_PGADMIN=true
            WITH_KIBANA=true
            shift
            ;;
        --rebuild)
            REBUILD=true
            shift
            ;;
        --help)
            echo "Usage: ./start-dev.sh [OPTIONS]"
            echo ""
            echo "Options:"
            echo "  --with-pgadmin    Start pgAdmin alongside PostgreSQL"
            echo "  --with-kibana     Start Kibana alongside Elasticsearch"
            echo "  --with-all        Start all optional services (pgAdmin + Kibana)"
            echo "  --rebuild         Rebuild containers before starting"
            echo "  --help            Show this help message"
            echo ""
            echo "Examples:"
            echo "  ./start-dev.sh                    # Start PostgreSQL, Elasticsearch, and Logstash"
            echo "  ./start-dev.sh --with-pgadmin     # Start with pgAdmin"
            echo "  ./start-dev.sh --with-kibana      # Start with Kibana"
            echo "  ./start-dev.sh --with-all         # Start with all optional services"
            echo "  ./start-dev.sh --rebuild          # Rebuild and start containers"
            exit 0
            ;;
        *)
            echo -e "${RED}Unknown option: $1${NC}"
            echo "Run './start-dev.sh --help' for usage information."
            exit 1
            ;;
    esac
done

# Stop any running containers
echo -e "${YELLOW}Stopping any running containers...${NC}"
docker-compose down

# Rebuild if requested
if [ "$REBUILD" = true ]; then
    echo -e "${YELLOW}Rebuilding containers...${NC}"
    docker-compose build --no-cache
fi

# Start containers
echo -e "${GREEN}Starting containers...${NC}"
PROFILES=""
if [ "$WITH_PGADMIN" = true ]; then
    PROFILES="$PROFILES --profile with-pgadmin"
fi
if [ "$WITH_KIBANA" = true ]; then
    PROFILES="$PROFILES --profile with-kibana"
fi

if [ -n "$PROFILES" ]; then
    docker-compose $PROFILES up -d
else
    docker-compose up -d postgres elasticsearch logstash
fi

# Wait for PostgreSQL to be healthy
echo -e "${YELLOW}Waiting for PostgreSQL to be ready...${NC}"
timeout 60 bash -c 'until docker exec knowledge-tracker-postgres-dev pg_isready -U ${POSTGRES_USER:-pragma_dev} > /dev/null 2>&1; do sleep 1; done'

if [ $? -ne 0 ]; then
    echo -e "${RED}Error: PostgreSQL failed to start within 60 seconds.${NC}"
    echo -e "${YELLOW}Check logs with: docker-compose logs postgres${NC}"
    exit 1
fi

echo -e "${GREEN}PostgreSQL is ready!${NC}"

# Wait for Elasticsearch to be healthy
echo -e "${YELLOW}Waiting for Elasticsearch to be ready...${NC}"
timeout 90 bash -c 'until curl -f http://localhost:${ES_PORT:-9200}/_cluster/health > /dev/null 2>&1; do sleep 2; done'

if [ $? -ne 0 ]; then
    echo -e "${RED}Error: Elasticsearch failed to start within 90 seconds.${NC}"
    echo -e "${YELLOW}Check logs with: docker-compose logs elasticsearch${NC}"
    exit 1
fi

echo -e "${GREEN}Elasticsearch is ready!${NC}"

# Wait for Logstash to be healthy
echo -e "${YELLOW}Waiting for Logstash to be ready...${NC}"
timeout 120 bash -c 'until curl -f http://localhost:9600/_node/stats > /dev/null 2>&1; do sleep 3; done'

if [ $? -ne 0 ]; then
    echo -e "${YELLOW}Warning: Logstash may not be fully ready yet. Check logs with: docker-compose logs logstash${NC}"
else
    echo -e "${GREEN}Logstash is ready!${NC}"
fi

echo ""
echo -e "${GREEN}================================================${NC}"
echo -e "${GREEN}Dev Environment Started Successfully!${NC}"
echo -e "${GREEN}================================================${NC}"
echo ""
echo -e "${GREEN}PostgreSQL Details:${NC}"
echo -e "  Host:     localhost"
echo -e "  Port:     ${POSTGRES_PORT:-5432}"
echo -e "  Database: ${POSTGRES_DB:-knowledge_tracker_dev}"
echo -e "  User:     ${POSTGRES_USER:-pragma_dev}"
echo -e "  Password: ${POSTGRES_PASSWORD:-pragma_dev_password}"
echo ""
echo -e "${GREEN}Elasticsearch Details:${NC}"
echo -e "  URL:      http://localhost:${ES_PORT:-9200}"
echo -e "  Health:   http://localhost:${ES_PORT:-9200}/_cluster/health"
echo -e "  Security: Disabled (development mode)"
echo ""
echo -e "${GREEN}Logstash Details:${NC}"
echo -e "  Monitoring API: http://localhost:9600"
echo -e "  Node Stats:     http://localhost:9600/_node/stats"
echo -e "  Pipelines:      5 (accounts, projects, pragmatics, knowledge, applied-knowledge)"
echo -e "  Sync Interval:  30-60 seconds"
echo ""

if [ "$WITH_PGADMIN" = true ]; then
    echo -e "${GREEN}pgAdmin Details:${NC}"
    echo -e "  URL:      http://localhost:${PGADMIN_PORT:-5050}"
    echo -e "  Email:    ${PGADMIN_EMAIL:-admin@pragma.com}"
    echo -e "  Password: ${PGADMIN_PASSWORD:-admin}"
    echo ""
fi

if [ "$WITH_KIBANA" = true ]; then
    echo -e "${GREEN}Kibana Details:${NC}"
    echo -e "  URL:      http://localhost:${KIBANA_PORT:-5601}"
    echo -e "  Note:     May take 1-2 minutes to fully initialize"
    echo ""
fi

echo -e "${GREEN}To start the application:${NC}"
echo -e "  ./gradlew bootRun --args='--spring.profiles.active=dev'"
echo ""
echo -e "${GREEN}To stop the containers:${NC}"
echo -e "  docker-compose down"
echo ""
echo -e "${GREEN}To view logs:${NC}"
echo -e "  docker-compose logs -f postgres elasticsearch logstash"
echo ""
echo -e "${GREEN}To monitor Logstash pipelines:${NC}"
echo -e "  curl http://localhost:9600/_node/stats/pipelines?pretty"
echo ""
