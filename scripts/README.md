# Scripts Directory

This directory contains utility scripts for the Knowledge Tracker application.

## Available Scripts

### seed-keycloak-users.sh

Seeds Keycloak with dummy users for development and testing. Creates users that match the data defined in `src/main/resources/sql/data-dev.sql` and `data-test.sql`.

**Usage:**

```bash
# Using default configuration
./scripts/seed-keycloak-users.sh

# With custom configuration
KEYCLOAK_URL=http://localhost:8180 \
KEYCLOAK_REALM=pragma-knowledge-tracker \
KEYCLOAK_ADMIN_USER=admin \
KEYCLOAK_ADMIN_PASSWORD=admin \
DEFAULT_USER_PASSWORD=CustomPassword123! \
./scripts/seed-keycloak-users.sh
```

**Environment Variables:**

| Variable | Default | Description |
|----------|---------|-------------|
| `KEYCLOAK_URL` | `http://localhost:8180` | Keycloak server URL |
| `KEYCLOAK_REALM` | `pragma-knowledge-tracker` | Target realm name |
| `KEYCLOAK_ADMIN_USER` | `admin` | Keycloak admin username |
| `KEYCLOAK_ADMIN_PASSWORD` | `admin` | Keycloak admin password |
| `DEFAULT_USER_PASSWORD` | `Pragma2024!` | Default password for all created users |

**Created Users:**

The script creates the following users:

| Email | Name | Role | Active |
|-------|------|------|--------|
| admin@pragma.com.co | System Administrator | FULL_ADMINISTRATOR | Yes |
| laura.diaz@pragma.com.co | Laura Díaz | FULL_ADMINISTRATOR | Yes |
| carlos.rodriguez@pragma.com.co | Carlos Rodríguez | KNOWLEDGE_MANAGER | Yes |
| sofia.gonzalez@pragma.com.co | Sofía González | KNOWLEDGE_MANAGER | Yes |
| juan.perez@pragma.com.co | Juan Pérez | PROJECT_ACCOUNT_MANAGER | Yes |
| maria.garcia@pragma.com.co | María García | PROJECT_ACCOUNT_MANAGER | Yes |
| jorge.sanchez@pragma.com.co | Jorge Sánchez | PEOPLE_MANAGER | Yes |
| paula.ramirez@pragma.com.co | Paula Ramírez | PEOPLE_MANAGER | Yes |
| luis.lopez@pragma.com.co | Luis López | USER | Yes |
| diego.hernandez@pragma.com.co | Diego Hernández | USER | Yes |
| camila.flores@pragma.com.co | Camila Flores | USER | Yes |
| santiago.parra@pragma.com.co | Santiago Parra | USER | Yes |
| natalia.moreno@pragma.com.co | Natalia Moreno | USER | Yes |
| andres.torres@pragma.com.co | Andrés Torres | USER | Yes |
| ana.martinez@pragma.com.co | Ana Martínez | USER | No (Inactive) |

**Prerequisites:**

- Keycloak must be running and accessible
- `curl` must be installed
- `jq` (optional, for better JSON formatting)

**Example Workflow:**

```bash
# 1. Start Keycloak and PostgreSQL
docker-compose up -d keycloak-postgres keycloak

# 2. Wait for Keycloak to be ready (script does this automatically)
# Or manually check:
curl http://localhost:8180/health/ready

# 3. Run the seeding script
./scripts/seed-keycloak-users.sh

# 4. Test authentication
curl -X POST http://localhost:8180/realms/pragma-knowledge-tracker/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=knowledge-tracker-api" \
  -d "client_secret=YOUR_CLIENT_SECRET" \
  -d "username=admin@pragma.com.co" \
  -d "password=Pragma2024!"
```

**Notes:**

- The script is idempotent - running it multiple times will skip existing users
- All users are created with `emailVerified=true`
- Passwords are non-temporary (users won't be forced to change on first login)
- The inactive user (ana.martinez@pragma.com.co) is created but disabled in Keycloak

## Other Scripts

### sync-postgres-to-es.sh

Synchronizes data from PostgreSQL to Elasticsearch (already exists in the project).

### update-api-versioning.sh

Updates API versioning across the project (already exists in the project).
