# Keycloak Integration - Quick Start Guide

This guide will help you set up and test the Keycloak integration with the Knowledge Tracker application.

## Prerequisites

- Docker and Docker Compose installed
- Java 21 installed
- Gradle 8.x (included via Gradle Wrapper)

## Step 1: Start the Infrastructure

```bash
# Start all services (PostgreSQL, Keycloak, Elasticsearch, etc.)
docker-compose up -d

# Or start only the essential services (PostgreSQL, Keycloak)
docker-compose up -d postgres keycloak-postgres keycloak

# Monitor Keycloak startup (takes ~60-90 seconds)
docker-compose logs -f keycloak
```

Wait until you see:
```
Keycloak 26.0.7 on JVM (powered by Quarkus) started in Xms
Listening on: http://0.0.0.0:8080
```

## Step 2: Configure Keycloak Client Secret

1. **Access Keycloak Admin Console**:
   - Open: http://localhost:8180
   - Username: `admin`
   - Password: `admin`

2. **Select the Realm**:
   - Click the dropdown at the top-left (currently showing "master")
   - Select: `pragma-knowledge-tracker`

3. **Get Client Secret**:
   - Navigate to: **Clients** (left menu)
   - Click on: `knowledge-tracker-api`
   - Go to: **Credentials** tab
   - Copy the **Secret** value

4. **Update .env file**:
   ```bash
   # Edit .env file
   nano .env

   # Update this line with the copied secret:
   KEYCLOAK_CLIENT_SECRET=<paste-your-secret-here>
   ```

## Step 3: Start the Application

```bash
# Build the application
./gradlew clean build

# Run with dev profile (Keycloak integration enabled)
./gradlew bootRun --args='--spring.profiles.active=dev'
```

The application will start on http://localhost:8080

## Step 4: Register a Test User

### Option A: Using the API

```bash
curl -X POST http://localhost:8080/api/v1/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@pragma.com.co",
    "firstName": "John",
    "lastName": "Doe",
    "systemRole": "USER",
    "password": "SecurePassword123",
    "temporaryPassword": false
  }'
```

Expected response (HTTP 201):
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "email": "john.doe@pragma.com.co",
  "firstName": "John",
  "lastName": "Doe",
  "systemRole": "USER",
  "active": true,
  "keycloakId": "f47ac10b-58cc-4372-a567-0e02b2c3d479"
}
```

This will:
1. ✅ Create the user in the application database
2. ✅ Create the user in Keycloak
3. ✅ Set the password in Keycloak
4. ✅ Publish a `UserRegisteredEvent`

### Option B: Using Keycloak Admin Console

1. Navigate to: **Users** → **Add user**
2. Fill in the form:
   - Username: `jane.smith@pragma.com.co`
   - Email: `jane.smith@pragma.com.co`
   - Email Verified: **ON**
   - First Name: `Jane`
   - Last Name: `Smith`
3. Click **Save**
4. Set credentials:
   - Go to **Credentials** tab
   - Click **Set password**
   - Enter password: `SecurePassword123`
   - Temporary: **OFF**
   - Click **Save**
5. Assign roles:
   - Go to **Role Mappings** tab
   - Select **Realm roles**: `user` (or any other role)
   - Click **Assign**

**Note**: Users created via Admin Console will NOT be in the application database automatically. You'll need to sync them or they won't be able to use application-specific features.

## Step 5: Test Authentication

### Get Access Token

```bash
# Set your client secret (from Step 2)
export CLIENT_SECRET="your-client-secret-here"

# Get access token
curl -X POST http://localhost:8180/realms/pragma-knowledge-tracker/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=knowledge-tracker-api" \
  -d "client_secret=$CLIENT_SECRET" \
  -d "username=john.doe@pragma.com.co" \
  -d "password=SecurePassword123" | jq
```

Expected response:
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cC...",
  "expires_in": 300,
  "refresh_expires_in": 1800,
  "refresh_token": "eyJhbGciOiJIUzUxMiIsInR5cC...",
  "token_type": "Bearer",
  "not-before-policy": 0,
  "session_state": "uuid",
  "scope": "profile email"
}
```

### Decode and Verify JWT

```bash
# Extract access token
export ACCESS_TOKEN="<copy-access-token-from-above>"

# Decode using jwt.io or base64
echo $ACCESS_TOKEN | cut -d'.' -f2 | base64 -d 2>/dev/null | jq
```

Verify the token contains:
```json
{
  "exp": 1234567890,
  "iat": 1234567890,
  "iss": "http://localhost:8180/realms/pragma-knowledge-tracker",
  "sub": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "typ": "Bearer",
  "azp": "knowledge-tracker-api",
  "email": "john.doe@pragma.com.co",
  "preferred_username": "john.doe@pragma.com.co",
  "realm_roles": [
    "user",
    "default-roles-pragma-knowledge-tracker"
  ]
}
```

### Use Token to Access Protected API

```bash
# Call a protected endpoint (when implemented)
curl -X GET http://localhost:8080/api/v1/some-protected-endpoint \
  -H "Authorization: Bearer $ACCESS_TOKEN"
```

Currently, the dev profile has security disabled (`permitAll`), so you won't see authentication in action yet. To test with security enabled, use the prod profile configuration.

## Step 6: Test User Registration Flow End-to-End

Let's verify the complete registration flow works:

```bash
# 1. Register a user
curl -X POST http://localhost:8080/api/v1/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test.complete@pragma.com.co",
    "firstName": "Complete",
    "lastName": "Test",
    "systemRole": "KNOWLEDGE_MANAGER",
    "password": "TestPassword123",
    "temporaryPassword": false
  }' | jq

# 2. Verify user in application database
# (Connect to PostgreSQL and check "User" table)

# 3. Verify user in Keycloak
# Open: http://localhost:8180/admin/master/console/#/pragma-knowledge-tracker/users
# Search for: test.complete@pragma.com.co

# 4. Test authentication
curl -X POST http://localhost:8180/realms/pragma-knowledge-tracker/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=knowledge-tracker-api" \
  -d "client_secret=$CLIENT_SECRET" \
  -d "username=test.complete@pragma.com.co" \
  -d "password=TestPassword123" | jq
```

## Available System Roles

The application defines 5 system roles (mapped to Keycloak realm roles):

| Role | Description | Permissions |
|------|-------------|-------------|
| `FULL_ADMINISTRATOR` | Full system access | All 12 permissions |
| `KNOWLEDGE_MANAGER` | Manage knowledge catalog | CREATE/UPDATE_KNOWLEDGE, CREATE/UPDATE_APPLIED_KNOWLEDGE, VIEW_ALL |
| `PROJECT_ACCOUNT_MANAGER` | Manage projects and accounts | CREATE/UPDATE_PROJECTS, CREATE/UPDATE_ACCOUNTS, VIEW_ALL |
| `PEOPLE_MANAGER` | Manage employees | CREATE/UPDATE_PRAGMATICS, VIEW_ALL |
| `USER` | Read-only access | VIEW_ALL |

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                    User Registration Flow                    │
└─────────────────────────────────────────────────────────────┘

POST /api/v1/users/register
         │
         ▼
┌─────────────────────────┐
│  UserManagementController│
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────┐
│  RegisterUserUseCase    │  (Application Layer)
└────────────┬────────────┘
             │
        ┌────┴─────┬──────────────┬────────────┐
        │          │              │            │
        ▼          ▼              ▼            ▼
   ┌────────┐ ┌────────┐   ┌──────────┐  ┌─────────┐
   │Validate│ │  Save  │   │ Create   │  │ Publish │
   │ Email  │ │   to   │   │ user in  │  │  Event  │
   │        │ │  DB    │   │ Keycloak │  │         │
   └────────┘ └────────┘   └──────────┘  └─────────┘
                                │
                                ▼
                    ┌──────────────────────┐
                    │ KeycloakUserService  │  (Port)
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │KeycloakUserAdapter   │  (Infrastructure)
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │  Keycloak Admin API  │
                    └──────────────────────┘
```

## Troubleshooting

### Keycloak not starting

```bash
# Check logs
docker-compose logs keycloak

# Restart Keycloak
docker-compose restart keycloak

# Rebuild and restart
docker-compose up -d --force-recreate keycloak
```

### User registration fails

Check application logs for errors:
- Database connection issues
- Keycloak connection failures
- Validation errors

```bash
# Application logs
./gradlew bootRun --args='--spring.profiles.active=dev'
```

### Authentication fails

```bash
# Verify Keycloak is accessible
curl http://localhost:8180/health/ready

# Verify realm exists
curl http://localhost:8180/realms/pragma-knowledge-tracker

# Check token endpoint
curl http://localhost:8180/realms/pragma-knowledge-tracker/.well-known/openid-configuration | jq
```

### Client secret not working

1. Verify you copied the correct secret from Keycloak Admin Console
2. Ensure .env file is loaded (restart application after changing .env)
3. Check application-dev.yml has correct Keycloak configuration

## Next Steps

1. **Implement Protected Endpoints**: Add security annotations to REST controllers
2. **Role-Based Authorization**: Use `@PreAuthorize` with role checks
3. **Frontend Integration**: Implement OAuth2 login flow in frontend
4. **Token Refresh**: Implement refresh token handling
5. **Logout**: Implement logout endpoint
6. **User Profile**: Add endpoint to get current user profile
7. **Password Reset**: Implement password reset flow

## Additional Resources

- [Keycloak README](./keycloak/README.md) - Detailed Keycloak configuration
- [CLAUDE.md](./CLAUDE.md) - Project architecture and guidelines
- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [Spring Security OAuth2](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html)
