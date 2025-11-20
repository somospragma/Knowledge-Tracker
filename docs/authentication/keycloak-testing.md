# Keycloak Authentication Testing Guide

## Quick Start

### 1. Get Client Secret

**Option A: Via Keycloak Admin Console (Easiest)**

1. Open: http://localhost:8180
2. Login with:
   - Username: `admin`
   - Password: `admin`
3. Select realm: `pragma-knowledge-tracker` (dropdown in top-left)
4. Navigate to: **Clients** → **knowledge-tracker-api**
5. Click on the **Credentials** tab
6. Copy the **Secret** value

**Option B: Regenerate a New Secret**

If you want to set a custom secret:

1. Follow steps 1-4 above
2. In the **Credentials** tab, click **Regenerate Secret**
3. Copy the new secret value
4. Update your `.env` file:
   ```bash
   KEYCLOAK_CLIENT_SECRET=your-new-secret
   ```

### 2. Test Authentication

#### Option A: Using the Test Script (Automated)

```bash
# Set the client secret first
export KEYCLOAK_CLIENT_SECRET="your-secret-here"

# Run the test script
./scripts/test-keycloak-auth.sh
```

This will test all 15 users automatically and show you which ones can authenticate.

#### Option B: Manual Testing with curl

```bash
# Replace YOUR_CLIENT_SECRET with the actual secret
curl -X POST http://localhost:8180/realms/pragma-knowledge-tracker/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=knowledge-tracker-api" \
  -d "client_secret=YOUR_CLIENT_SECRET" \
  -d "username=admin@pragma.com.co" \
  -d "password=Pragma2024!" | jq
```

**Expected Response:**
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expires_in": 300,
  "refresh_expires_in": 1800,
  "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "token_type": "Bearer",
  "not-before-policy": 0,
  "session_state": "a1b2c3d4-...",
  "scope": "profile email"
}
```

### 3. Decode the JWT Token

Once you have an access token, you can decode it to see the user info and roles:

**Option A: Using jwt.io**

1. Go to https://jwt.io
2. Paste your `access_token` in the "Encoded" section
3. View the decoded payload on the right

**Option B: Using command line (with jq)**

```bash
# Save your token
TOKEN="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9..."

# Decode the payload (middle part of JWT)
echo $TOKEN | cut -d'.' -f2 | base64 -d | jq
```

**Expected Payload:**
```json
{
  "exp": 1700000000,
  "iat": 1700000000,
  "jti": "...",
  "iss": "http://localhost:8180/realms/pragma-knowledge-tracker",
  "sub": "a0000000-0000-0000-0000-000000000001",
  "typ": "Bearer",
  "azp": "knowledge-tracker-api",
  "session_state": "...",
  "preferred_username": "admin@pragma.com.co",
  "email_verified": true,
  "name": "System Administrator",
  "given_name": "System",
  "family_name": "Administrator",
  "email": "admin@pragma.com.co",
  "realm_access": {
    "roles": [
      "full_administrator",
      "default-roles-pragma-knowledge-tracker",
      "offline_access",
      "uma_authorization"
    ]
  }
}
```

### 4. Use Token to Access Protected Endpoints

```bash
# Get token
TOKEN=$(curl -s -X POST http://localhost:8180/realms/pragma-knowledge-tracker/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=knowledge-tracker-api" \
  -d "client_secret=YOUR_CLIENT_SECRET" \
  -d "username=admin@pragma.com.co" \
  -d "password=Pragma2024!" | jq -r '.access_token')

# Use the token to call a protected API endpoint
curl -X GET http://localhost:8080/api/v1/users \
  -H "Authorization: Bearer $TOKEN"
```

## Available Test Users

All users have the password: **Pragma2024!**

| Email | Name | Role | Enabled |
|-------|------|------|---------|
| admin@pragma.com.co | System Administrator | FULL_ADMINISTRATOR | ✓ |
| laura.diaz@pragma.com.co | Laura Díaz | FULL_ADMINISTRATOR | ✓ |
| carlos.rodriguez@pragma.com.co | Carlos Rodríguez | KNOWLEDGE_MANAGER | ✓ |
| sofia.gonzalez@pragma.com.co | Sofía González | KNOWLEDGE_MANAGER | ✓ |
| juan.perez@pragma.com.co | Juan Pérez | PROJECT_ACCOUNT_MANAGER | ✓ |
| maria.garcia@pragma.com.co | María García | PROJECT_ACCOUNT_MANAGER | ✓ |
| jorge.sanchez@pragma.com.co | Jorge Sánchez | PEOPLE_MANAGER | ✓ |
| paula.ramirez@pragma.com.co | Paula Ramírez | PEOPLE_MANAGER | ✓ |
| luis.lopez@pragma.com.co | Luis López | USER | ✓ |
| diego.hernandez@pragma.com.co | Diego Hernández | USER | ✓ |
| camila.flores@pragma.com.co | Camila Flores | USER | ✓ |
| santiago.parra@pragma.com.co | Santiago Parra | USER | ✓ |
| natalia.moreno@pragma.com.co | Natalia Moreno | USER | ✓ |
| andres.torres@pragma.com.co | Andrés Torres | USER | ✓ |
| ana.martinez@pragma.com.co | Ana Martínez | USER | ✗ (Inactive) |

## Refresh Token Flow

If you have a refresh token, you can get a new access token without re-authenticating:

```bash
curl -X POST http://localhost:8180/realms/pragma-knowledge-tracker/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=refresh_token" \
  -d "client_id=knowledge-tracker-api" \
  -d "client_secret=YOUR_CLIENT_SECRET" \
  -d "refresh_token=YOUR_REFRESH_TOKEN"
```

## Troubleshooting

### "Invalid credentials" error

1. Verify the user exists in Keycloak Admin Console
2. Check the password is correct: `Pragma2024!`
3. Ensure the user is enabled
4. Check the realm name is correct: `pragma-knowledge-tracker`

### "Invalid client credentials" error

1. Verify the client secret is correct
2. Check the client ID is: `knowledge-tracker-api`
3. Ensure Direct Access Grants is enabled for the client

### "User is disabled" error

This is expected for `ana.martinez@pragma.com.co` - she's the test inactive user.

### Token expired error

Access tokens expire after 5 minutes (300 seconds). Use the refresh token to get a new access token.

## Testing Role-Based Access

Each role has different permissions. Test with different users:

```bash
# Test as Full Administrator
curl ... -d "username=admin@pragma.com.co" -d "password=Pragma2024!"

# Test as Knowledge Manager
curl ... -d "username=carlos.rodriguez@pragma.com.co" -d "password=Pragma2024!"

# Test as Regular User
curl ... -d "username=luis.lopez@pragma.com.co" -d "password=Pragma2024!"
```

Then decode the JWT token to see the different roles in the `realm_access.roles` claim.

## Next Steps

1. Configure your application to validate JWT tokens from Keycloak
2. Implement role-based authorization using the roles in the token
3. Set up Spring Security OAuth2 Resource Server (if using Spring Boot)
4. Test protected endpoints with different user roles

## Additional Resources

- Keycloak Admin Console: http://localhost:8180
- Keycloak Documentation: https://www.keycloak.org/documentation
- JWT Debugger: https://jwt.io
- OAuth 2.0 Playground: https://www.oauth.com/playground/
