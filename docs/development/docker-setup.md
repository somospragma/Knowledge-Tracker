# Docker Development Environment Setup

This guide explains how to set up and use the PostgreSQL database for development using Docker Compose.

## Prerequisites

- Docker installed and running
- Docker Compose installed (included with Docker Desktop)

## Quick Start

### 1. Configure Environment Variables

The `.env` file has already been created from `.env.example`. You can modify it if needed:

```bash
# Edit .env to customize database credentials (optional)
nano .env
```

### 2. Start the Development Environment

```bash
# Start PostgreSQL only
./start-dev.sh

# Start PostgreSQL and pgAdmin
./start-dev.sh --with-pgadmin

# Rebuild containers before starting
./start-dev.sh --rebuild
```

### 3. Run the Application

Once the database is running, start the Spring Boot application with the dev profile:

```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

## Database Access

### PostgreSQL Connection Details

- **Host:** localhost
- **Port:** 5432 (configurable in `.env`)
- **Database:** knowledge_tracker_dev
- **Username:** pragma_dev
- **Password:** pragma_dev_password

### pgAdmin (Optional)

If you started the environment with `--with-pgadmin`:

- **URL:** http://localhost:5050
- **Email:** admin@pragma.com
- **Password:** admin

#### Adding PostgreSQL Server in pgAdmin

1. Open pgAdmin at http://localhost:5050
2. Right-click "Servers" → "Register" → "Server"
3. **General Tab:**
   - Name: Knowledge Tracker Dev
4. **Connection Tab:**
   - Host: postgres (Docker service name)
   - Port: 5432
   - Maintenance database: knowledge_tracker_dev
   - Username: pragma_dev
   - Password: pragma_dev_password

## Docker Management Commands

### View Logs

```bash
# View PostgreSQL logs
docker-compose logs -f postgres

# View all logs
docker-compose logs -f
```

### Stop Containers

```bash
# Stop all containers
docker-compose down

# Stop and remove volumes (WARNING: This deletes all data!)
docker-compose down -v
```

### Restart Containers

```bash
# Restart PostgreSQL
docker-compose restart postgres

# Restart all containers
docker-compose restart
```

### Check Container Status

```bash
# View running containers
docker-compose ps

# View all containers (including stopped)
docker-compose ps -a
```

## Database Initialization

The database will be automatically initialized with:

1. **Schema Creation:** Handled by Hibernate (ddl-auto: update)
2. **Data Population:** SQL scripts from `src/main/resources/sql/data-dev.sql`

### Manual SQL Execution

You can execute SQL commands directly in the PostgreSQL container:

```bash
# Connect to PostgreSQL CLI
docker exec -it knowledge-tracker-postgres-dev psql -U pragma_dev -d knowledge_tracker_dev

# Example queries
\dt                                    # List all tables
SELECT * FROM accounts;                # Query data
\q                                     # Exit
```

## Troubleshooting

### Port Already in Use

If port 5432 is already in use:

1. Edit `.env` file
2. Change `POSTGRES_PORT=5432` to another port (e.g., `POSTGRES_PORT=5433`)
3. Update `DB_PORT` accordingly
4. Restart: `./start-dev.sh`

### Database Connection Failed

1. Check if Docker is running: `docker info`
2. Check if PostgreSQL container is healthy: `docker-compose ps`
3. View PostgreSQL logs: `docker-compose logs postgres`
4. Verify environment variables in `.env` match `application-dev.yml`

### Reset Database

To completely reset the database:

```bash
# Stop containers and remove volumes
docker-compose down -v

# Start fresh
./start-dev.sh
```

### Container Won't Start

```bash
# View detailed logs
docker-compose logs postgres

# Rebuild containers
./start-dev.sh --rebuild

# Or manually rebuild
docker-compose build --no-cache
docker-compose up -d
```

## Production Notes

**⚠️ IMPORTANT:** This setup is for **development only**. Do NOT use these configurations in production:

- Default passwords are weak and publicly known
- No SSL/TLS encryption configured
- Database is exposed on localhost without additional security
- No backup or replication strategy
- Data persistence is on local Docker volumes

For production deployment:
- Use strong, unique passwords
- Enable SSL/TLS for database connections
- Use managed database services (AWS RDS, Azure Database, etc.)
- Implement proper backup and disaster recovery
- Use secrets management (AWS Secrets Manager, HashiCorp Vault, etc.)
- Never commit `.env` file to version control

## Environment Variables Reference

### Docker Compose Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `POSTGRES_DB` | knowledge_tracker_dev | Database name |
| `POSTGRES_USER` | pragma_dev | Database user |
| `POSTGRES_PASSWORD` | pragma_dev_password | Database password |
| `POSTGRES_PORT` | 5432 | Host port for PostgreSQL |
| `PGADMIN_EMAIL` | admin@pragma.com | pgAdmin login email |
| `PGADMIN_PASSWORD` | admin | pgAdmin login password |
| `PGADMIN_PORT` | 5050 | Host port for pgAdmin |

### Application Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `DB_HOST` | localhost | Database host |
| `DB_PORT` | 5432 | Database port |
| `DB_NAME` | knowledge_tracker_dev | Database name |
| `DB_USERNAME` | pragma_dev | Database username |
| `DB_PASSWORD` | pragma_dev_password | Database password |
| `DB_POOL_SIZE` | 5 | Connection pool size |
| `DB_MIN_IDLE` | 2 | Minimum idle connections |
| `DB_CONNECTION_TIMEOUT` | 30000 | Connection timeout (ms) |
| `SERVER_PORT` | 8080 | Application server port |

## Additional Resources

- [Docker Documentation](https://docs.docker.com/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Spring Boot Docker Guide](https://spring.io/guides/gs/spring-boot-docker/)
- [pgAdmin Documentation](https://www.pgadmin.org/docs/)
