# API Testing Guide - User Management

## RegisterUserUseCase Testing

### Endpoint

```
POST http://localhost:8080/api/v1/users/register
```

### Request Headers

```
Content-Type: application/json
```

### Request Body Schema

```json
{
  "email": "string (required, valid email format)",
  "firstName": "string (required, max 100 chars)",
  "lastName": "string (required, max 100 chars)",
  "systemRole": "string (required, one of: FULL_ADMINISTRATOR, KNOWLEDGE_MANAGER, PROJECT_ACCOUNT_MANAGER, PEOPLE_MANAGER, USER)",
  "password": "string (required, min 8 chars)",
  "temporaryPassword": "boolean (optional, default: false)"
}
```

---

## CURL Commands

### 1. Register a Full Administrator

```bash
curl -X POST http://localhost:8080/api/v1/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "new.admin@pragma.com.co",
    "firstName": "New",
    "lastName": "Administrator",
    "systemRole": "FULL_ADMINISTRATOR",
    "password": "SecurePassword123!",
    "temporaryPassword": false
  }'
```

**Expected Response (HTTP 201):**
```json
{
  "id": "uuid",
  "email": "new.admin@pragma.com.co",
  "firstName": "New",
  "lastName": "Administrator",
  "systemRole": "FULL_ADMINISTRATOR",
  "active": true
}
```

---

### 2. Register a Knowledge Manager

```bash
curl -X POST http://localhost:8080/api/v1/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "knowledge.manager@pragma.com.co",
    "firstName": "Knowledge",
    "lastName": "Manager",
    "systemRole": "KNOWLEDGE_MANAGER",
    "password": "SecurePassword123!",
    "temporaryPassword": false
  }'
```

---

### 3. Register a Project/Account Manager

```bash
curl -X POST http://localhost:8080/api/v1/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "project.manager@pragma.com.co",
    "firstName": "Project",
    "lastName": "Manager",
    "systemRole": "PROJECT_ACCOUNT_MANAGER",
    "password": "SecurePassword123!",
    "temporaryPassword": false
  }'
```

---

### 4. Register a People Manager

```bash
curl -X POST http://localhost:8080/api/v1/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "people.manager@pragma.com.co",
    "firstName": "People",
    "lastName": "Manager",
    "systemRole": "PEOPLE_MANAGER",
    "password": "SecurePassword123!",
    "temporaryPassword": false
  }'
```

---

### 5. Register a Regular User

```bash
curl -X POST http://localhost:8080/api/v1/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "regular.user@pragma.com.co",
    "firstName": "Regular",
    "lastName": "User",
    "systemRole": "USER",
    "password": "SecurePassword123!",
    "temporaryPassword": false
  }'
```

---

### 6. Register with Temporary Password

```bash
curl -X POST http://localhost:8080/api/v1/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "temp.user@pragma.com.co",
    "firstName": "Temporary",
    "lastName": "User",
    "systemRole": "USER",
    "password": "TempPassword123!",
    "temporaryPassword": true
  }'
```

**Note:** User will be forced to change password on first login in Keycloak.

---

## Error Cases

### Duplicate Email (HTTP 409)

```bash
curl -X POST http://localhost:8080/api/v1/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@pragma.com.co",
    "firstName": "Duplicate",
    "lastName": "User",
    "systemRole": "USER",
    "password": "SecurePassword123!",
    "temporaryPassword": false
  }'
```

**Expected Response:**
```json
{
  "timestamp": "2024-11-20T10:30:00",
  "status": 409,
  "error": "Conflict",
  "message": "User with email admin@pragma.com.co already exists",
  "path": "/api/v1/users/register"
}
```

---

### Invalid Email Format (HTTP 400)

```bash
curl -X POST http://localhost:8080/api/v1/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "invalid-email",
    "firstName": "Invalid",
    "lastName": "Email",
    "systemRole": "USER",
    "password": "SecurePassword123!",
    "temporaryPassword": false
  }'
```

**Expected Response:**
```json
{
  "timestamp": "2024-11-20T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Email must be valid",
  "path": "/api/v1/users/register"
}
```

---

### Missing Required Field (HTTP 400)

```bash
curl -X POST http://localhost:8080/api/v1/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "missing.field@pragma.com.co",
    "lastName": "Missing",
    "systemRole": "USER",
    "password": "SecurePassword123!",
    "temporaryPassword": false
  }'
```

**Expected Response:**
```json
{
  "timestamp": "2024-11-20T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "First name is required",
  "path": "/api/v1/users/register"
}
```

---

### Password Too Short (HTTP 400)

```bash
curl -X POST http://localhost:8080/api/v1/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "short.password@pragma.com.co",
    "firstName": "Short",
    "lastName": "Password",
    "systemRole": "USER",
    "password": "short",
    "temporaryPassword": false
  }'
```

**Expected Response:**
```json
{
  "timestamp": "2024-11-20T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Password must be at least 8 characters",
  "path": "/api/v1/users/register"
}
```

---

### Invalid System Role (HTTP 400)

```bash
curl -X POST http://localhost:8080/api/v1/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "invalid.role@pragma.com.co",
    "firstName": "Invalid",
    "lastName": "Role",
    "systemRole": "SUPER_ADMIN",
    "password": "SecurePassword123!",
    "temporaryPassword": false
  }'
```

**Expected Response:**
```json
{
  "timestamp": "2024-11-20T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid system role: SUPER_ADMIN",
  "path": "/api/v1/users/register"
}
```

---

## Automated Testing

### Run All Test Cases

```bash
./scripts/test-register-user.sh
```

This will test:
- ✓ All 5 valid system roles
- ✓ Duplicate email detection
- ✓ Invalid email format
- ✓ Missing required fields
- ✓ Password length validation
- ✓ Invalid system role
- ✓ Temporary password flag

---

## Verify Registration

### 1. Check Database

```bash
# Connect to PostgreSQL
docker exec -it knowledge-tracker-postgres-dev psql -U pragma_dev -d knowledge_tracker_dev

# Query users
SELECT id, email, first_name, last_name, system_role, active FROM "User";
```

### 2. Check Keycloak

**Via Admin Console:**
1. Open: http://localhost:8180
2. Login: `admin` / `admin`
3. Realm: `pragma-knowledge-tracker`
4. Navigate to: **Users**
5. Search for the newly created user

**Via API:**
```bash
# Get admin token
ADMIN_TOKEN=$(curl -s -X POST http://localhost:8180/realms/master/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=admin" \
  -d "password=admin" \
  -d "grant_type=password" \
  -d "client_id=admin-cli" | jq -r '.access_token')

# Search for user
curl -s -X GET "http://localhost:8180/admin/realms/pragma-knowledge-tracker/users?email=new.admin@pragma.com.co" \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq
```

### 3. Test Authentication

After registering a user, test if they can authenticate:

```bash
# Replace YOUR_CLIENT_SECRET with actual secret
curl -X POST http://localhost:8180/realms/pragma-knowledge-tracker/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=knowledge-tracker-api" \
  -d "client_secret=YOUR_CLIENT_SECRET" \
  -d "username=new.admin@pragma.com.co" \
  -d "password=SecurePassword123!" | jq
```

---

## System Roles and Permissions

| Role | Description | Permissions |
|------|-------------|-------------|
| **FULL_ADMINISTRATOR** | Complete system access | All permissions |
| **KNOWLEDGE_MANAGER** | Manage knowledge catalog | Create/Update knowledge and applied knowledge |
| **PROJECT_ACCOUNT_MANAGER** | Manage projects & accounts | Create/Update projects and accounts |
| **PEOPLE_MANAGER** | Manage Pragmatics | Create/Update Pragmatics (employees) |
| **USER** | Read-only access | View all data |

---

## Integration with Keycloak

When you register a user via this API:

1. ✅ User is created in PostgreSQL database (`User` table)
2. ✅ User is created in Keycloak
3. ✅ Appropriate realm role is assigned in Keycloak
4. ✅ Email is marked as verified in Keycloak
5. ✅ Password is set (permanent or temporary)
6. ✅ `UserRegisteredEvent` is published

---

## API Documentation

View the interactive API documentation:

**Swagger UI:** http://localhost:8080/swagger-ui.html

Look for the **User Management** tag to see all available endpoints.

---

## Troubleshooting

### Application not running

```bash
# Check if Spring Boot app is running
curl http://localhost:8080/actuator/health

# If not running, start it
./gradlew bootRun
```

### Keycloak not accessible

```bash
# Check Keycloak status
docker ps | grep keycloak

# Check Keycloak logs
docker logs knowledge-tracker-keycloak-dev

# Restart Keycloak if needed
docker-compose restart keycloak
```

### Database connection issues

```bash
# Check PostgreSQL status
docker ps | grep postgres

# Restart PostgreSQL if needed
docker-compose restart postgres
```

---

## Next Steps

After registering users, you can:

1. Test authentication (see `KEYCLOAK_TESTING.md`)
2. Verify roles are correctly assigned
3. Test role-based access control in your application
4. Integrate with frontend application
5. Add additional user management endpoints (update, delete, list)
