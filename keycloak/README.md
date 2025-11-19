# Keycloak Configuration for Knowledge Tracker

This directory contains the Keycloak configuration for the Knowledge Tracker application.

## Overview

Keycloak is used as the Identity and Access Management (IAM) solution for the Knowledge Tracker application. It provides:

- **User Authentication** - OAuth2/OpenID Connect authentication
- **User Management** - Centralized user directory
- **Role-Based Access Control (RBAC)** - Application-level authorization
- **Single Sign-On (SSO)** - Ready for future multi-application scenarios

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     Docker Compose Stack                     │
├─────────────────────────────────────────────────────────────┤
│                                                               │
│  ┌──────────────────┐         ┌──────────────────┐          │
│  │   Application    │         │    Keycloak      │          │
│  │   (Port 8080)    │◄────────┤   (Port 8180)    │          │
│  │                  │  JWT    │                  │          │
│  │  Spring Boot     │  Auth   │  Admin: 9990     │          │
│  └────────┬─────────┘         └────────┬─────────┘          │
│           │                            │                     │
│           │                            │                     │
│  ┌────────▼─────────┐         ┌────────▼─────────┐          │
│  │   PostgreSQL     │         │   PostgreSQL     │          │
│  │   (Port 5432)    │         │   (Internal)     │          │
│  │                  │         │                  │          │
│  │ knowledge_tracker│         │    keycloak DB   │          │
│  └──────────────────┘         └──────────────────┘          │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

## Realm Configuration

### Realm: `pragma-knowledge-tracker`

The realm is pre-configured with the following:

#### Realm Roles (Mapped to SystemRole enum)

1. **full_administrator** - Complete system access
2. **knowledge_manager** - Manage knowledge catalog and applied knowledge
3. **project_account_manager** - Manage projects and client accounts
4. **people_manager** - Manage Pragmatics (employees)
5. **user** (default) - Read-only access

#### Client: `knowledge-tracker-api`

- **Client ID**: `knowledge-tracker-api`
- **Protocol**: OpenID Connect
- **Access Type**: Confidential (requires client secret)
- **Authentication Flows**:
  - Standard Flow: Enabled (Authorization Code Flow)
  - Direct Access Grants: Enabled (Resource Owner Password Credentials)
  - Service Accounts: Enabled
- **Valid Redirect URIs**:
  - `http://localhost:8080/*`
  - `http://localhost:3000/*` (for future frontend)
- **Web Origins**:
  - `http://localhost:8080`
  - `http://localhost:3000`

#### JWT Token Mappers

The client includes mappers to include the following claims in JWT tokens:

- `realm_roles` - Array of realm roles assigned to the user
- `email` - User's email address
- `preferred_username` - Username (email)

## Starting Keycloak

### Using Docker Compose

```bash
# Start all services including Keycloak
docker-compose up -d

# Start only Keycloak and its database
docker-compose up -d keycloak-postgres keycloak

# View Keycloak logs
docker-compose logs -f keycloak

# Check Keycloak health
curl http://localhost:8180/health/ready
```

### First Time Setup

1. **Wait for Keycloak to start** (takes ~60-90 seconds)
   ```bash
   docker-compose logs -f keycloak
   ```

2. **Access Keycloak Admin Console**
   - URL: http://localhost:8180
   - Username: `admin`
   - Password: `admin`

3. **Verify Realm Import**
   - Select realm: `pragma-knowledge-tracker` (top-left dropdown)
   - Check that 5 realm roles exist
   - Verify client `knowledge-tracker-api` is configured

4. **Get Client Secret** (Required for application)
   - Navigate to: Clients → `knowledge-tracker-api` → Credentials tab
   - Copy the **Secret** value
   - Update `.env` file:
     ```
     KEYCLOAK_CLIENT_SECRET=<copied-secret>
     ```

## Creating Test Users

You can create test users via:

### Option 1: Keycloak Admin Console

1. Navigate to: Users → Add user
2. Fill in:
   - Username: `test.user@pragma.com.co`
   - Email: `test.user@pragma.com.co`
   - Email Verified: ON
   - First Name: `Test`
   - Last Name: `User`
3. Click **Save**
4. Go to **Credentials** tab → Set Password
5. Go to **Role Mappings** tab → Assign realm roles

### Option 2: Using the Application API

```bash
curl -X POST http://localhost:8080/api/v1/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test.user@pragma.com.co",
    "firstName": "Test",
    "lastName": "User",
    "systemRole": "USER",
    "password": "Password123",
    "temporaryPassword": false
  }'
```

This will:
1. Create the user in the application database
2. Create the user in Keycloak
3. Assign the appropriate role
4. Publish a `UserRegisteredEvent`

## Testing Authentication

### Get Access Token (Password Grant)

```bash
curl -X POST http://localhost:8180/realms/pragma-knowledge-tracker/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=knowledge-tracker-api" \
  -d "client_secret=<your-client-secret>" \
  -d "username=test.user@pragma.com.co" \
  -d "password=Password123"
```

Response:
```json
{
  "access_token": "eyJhbGc...",
  "expires_in": 300,
  "refresh_expires_in": 1800,
  "refresh_token": "eyJhbGc...",
  "token_type": "Bearer",
  "scope": "profile email"
}
```

### Decode JWT Token

Use https://jwt.io to decode the `access_token` and verify:
- `realm_roles` claim contains the assigned roles
- `email` claim contains the user's email
- Token is valid and signed by Keycloak

### Use Token to Access Protected API

```bash
curl -X GET http://localhost:8080/api/v1/protected-endpoint \
  -H "Authorization: Bearer <access-token>"
```

## Configuration Files

### realm-export.json

Pre-configured Keycloak realm with:
- Realm settings (token lifespans, session timeouts)
- Client configuration
- Realm roles
- Token mappers
- Security headers

**Note**: This file is imported automatically when Keycloak starts with the `--import-realm` flag.

## Environment Variables

### Docker Compose (.env)

```bash
# Keycloak Container
KEYCLOAK_PORT=8180                    # HTTP port
KEYCLOAK_ADMIN_PORT=9990              # Management port
KEYCLOAK_ADMIN_USER=admin             # Admin username
KEYCLOAK_ADMIN_PASSWORD=admin         # Admin password

# Keycloak Database
KEYCLOAK_DB=keycloak                  # Database name
KEYCLOAK_DB_USER=keycloak             # Database user
KEYCLOAK_DB_PASSWORD=keycloak_password # Database password
KEYCLOAK_LOG_LEVEL=info               # Log level

# Application Configuration
KEYCLOAK_SERVER_URL=http://localhost:8180
KEYCLOAK_REALM=pragma-knowledge-tracker
KEYCLOAK_CLIENT_ID=knowledge-tracker-api
KEYCLOAK_CLIENT_SECRET=                # Get from Admin Console
KEYCLOAK_ADMIN_CLIENT_ID=admin-cli
KEYCLOAK_ISSUER_URI=http://localhost:8180/realms/pragma-knowledge-tracker
KEYCLOAK_JWK_SET_URI=http://localhost:8180/realms/pragma-knowledge-tracker/protocol/openid-connect/certs
```

## Troubleshooting

### Keycloak won't start

```bash
# Check logs
docker-compose logs keycloak

# Common issues:
# - Database not ready: Wait for keycloak-postgres to be healthy
# - Port conflict: Check if port 8180 is already in use
# - Volume permissions: Check keycloak_data volume permissions
```

### Realm not imported

```bash
# Verify realm file exists
ls -la keycloak/realm-export.json

# Check Keycloak logs for import errors
docker-compose logs keycloak | grep -i import

# Manually import:
# 1. Access Admin Console
# 2. Click "Add realm" → Select file → Upload realm-export.json
```

### Cannot authenticate users

```bash
# Verify realm is selected
# Verify client secret is correct in .env
# Check application.yml Keycloak configuration
# Test token endpoint directly (see "Testing Authentication")
```

### JWT validation fails

```bash
# Verify issuer URI matches realm
# Check JWK set URI is accessible:
curl http://localhost:8180/realms/pragma-knowledge-tracker/protocol/openid-connect/certs

# Verify application-dev.yml JWT configuration matches realm URLs
```

## Security Notes

### Development Environment

The current setup is configured for **development only**:

- ✅ HTTP enabled (no HTTPS required)
- ✅ Hostname validation disabled
- ✅ Admin credentials in .env file
- ❌ **DO NOT use in production**

### Production Considerations

For production deployment:

1. **Enable HTTPS**: Set `KC_HOSTNAME_STRICT_HTTPS=true`
2. **Use proper hostname**: Set `KC_HOSTNAME=your-domain.com`
3. **Secure admin credentials**: Use strong passwords, store in secrets manager
4. **Enable SSL**: Configure SSL certificates
5. **External database**: Use managed PostgreSQL (AWS RDS, Azure Database)
6. **High availability**: Run multiple Keycloak instances behind load balancer
7. **Backup**: Regular database backups
8. **Monitoring**: Enable metrics and health checks

## Additional Resources

- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [Keycloak Server Administration Guide](https://www.keycloak.org/docs/latest/server_admin/)
- [Spring Security OAuth2 Resource Server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html)
- [OpenID Connect Core 1.0](https://openid.net/specs/openid-connect-core-1_0.html)
