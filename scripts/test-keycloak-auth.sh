#!/bin/bash

###############################################################################
# Keycloak Authentication Test Script
# Tests authentication for all seeded users
###############################################################################

# Configuration
KEYCLOAK_URL="${KEYCLOAK_URL:-http://localhost:8180}"
REALM="${KEYCLOAK_REALM:-pragma-knowledge-tracker}"
CLIENT_ID="${KEYCLOAK_CLIENT_ID:-knowledge-tracker-api}"
CLIENT_SECRET="${KEYCLOAK_CLIENT_SECRET}"
DEFAULT_PASSWORD="${DEFAULT_USER_PASSWORD:-Pragma2024!}"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}Keycloak Authentication Test${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# Check if client secret is provided
if [ -z "$CLIENT_SECRET" ]; then
    echo -e "${YELLOW}[WARNING]${NC} Client secret not provided via KEYCLOAK_CLIENT_SECRET env var"
    echo ""
    echo "To get the client secret:"
    echo "1. Open Keycloak Admin Console: http://localhost:8180"
    echo "2. Login with: admin / admin"
    echo "3. Select realm: pragma-knowledge-tracker"
    echo "4. Go to: Clients → knowledge-tracker-api → Credentials tab"
    echo "5. Copy the Secret value"
    echo ""
    echo "Then run this script with:"
    echo "KEYCLOAK_CLIENT_SECRET=your-secret ./scripts/test-keycloak-auth.sh"
    echo ""

    # Try to get it via admin API
    echo -e "${BLUE}[INFO]${NC} Attempting to retrieve client secret via Admin API..."

    # Get admin token
    ADMIN_TOKEN=$(curl -s -X POST "${KEYCLOAK_URL}/realms/master/protocol/openid-connect/token" \
        -H "Content-Type: application/x-www-form-urlencoded" \
        -d "username=admin" \
        -d "password=admin" \
        -d "grant_type=password" \
        -d "client_id=admin-cli" 2>/dev/null | grep -o '"access_token":"[^"]*' | cut -d'"' -f4)

    if [ ! -z "$ADMIN_TOKEN" ]; then
        CLIENT_SECRET=$(curl -s -X GET "${KEYCLOAK_URL}/admin/realms/${REALM}/clients/${CLIENT_ID}/client-secret" \
            -H "Authorization: Bearer $ADMIN_TOKEN" 2>/dev/null | grep -o '"value":"[^"]*' | cut -d'"' -f4)

        if [ ! -z "$CLIENT_SECRET" ] && [ "$CLIENT_SECRET" != "**********" ]; then
            echo -e "${GREEN}[SUCCESS]${NC} Retrieved client secret: ${CLIENT_SECRET}"
            echo ""
        else
            echo -e "${RED}[ERROR]${NC} Could not retrieve client secret automatically"
            exit 1
        fi
    else
        echo -e "${RED}[ERROR]${NC} Could not authenticate as admin to retrieve secret"
        exit 1
    fi
fi

# Function to test authentication for a user
test_user_auth() {
    local username=$1
    local user_display=$2

    echo -e "${BLUE}Testing:${NC} $user_display ($username)"

    RESPONSE=$(curl -s -w "\nHTTP_CODE:%{http_code}" -X POST "${KEYCLOAK_URL}/realms/${REALM}/protocol/openid-connect/token" \
        -H "Content-Type: application/x-www-form-urlencoded" \
        -d "grant_type=password" \
        -d "client_id=${CLIENT_ID}" \
        -d "client_secret=${CLIENT_SECRET}" \
        -d "username=${username}" \
        -d "password=${DEFAULT_PASSWORD}" 2>&1)

    HTTP_CODE=$(echo "$RESPONSE" | grep "HTTP_CODE" | cut -d':' -f2)
    BODY=$(echo "$RESPONSE" | grep -v "HTTP_CODE")

    if [ "$HTTP_CODE" = "200" ]; then
        # Extract access token (first 50 chars)
        ACCESS_TOKEN=$(echo "$BODY" | grep -o '"access_token":"[^"]*' | cut -d'"' -f4 | cut -c1-50)

        if [ ! -z "$ACCESS_TOKEN" ]; then
            echo -e "  ${GREEN}✓ SUCCESS${NC} - Token: ${ACCESS_TOKEN}..."

            # Decode JWT to show roles (if jq is available)
            if command -v jq &> /dev/null; then
                ROLES=$(echo "$BODY" | jq -r '.access_token' 2>/dev/null | cut -d'.' -f2 | base64 -d 2>/dev/null | jq -r '.realm_access.roles[]? // empty' 2>/dev/null | grep -E 'full_administrator|knowledge_manager|project_account_manager|people_manager|user' | head -1)
                if [ ! -z "$ROLES" ]; then
                    echo -e "  ${BLUE}Role:${NC} $ROLES"
                fi
            fi
        else
            echo -e "  ${YELLOW}⚠ WARNING${NC} - Got 200 but no access token in response"
        fi
    elif [ "$HTTP_CODE" = "401" ]; then
        ERROR=$(echo "$BODY" | grep -o '"error_description":"[^"]*' | cut -d'"' -f4)
        echo -e "  ${RED}✗ FAILED${NC} - Invalid credentials or user disabled"
        if [ ! -z "$ERROR" ]; then
            echo -e "  ${RED}Error:${NC} $ERROR"
        fi
    else
        echo -e "  ${RED}✗ FAILED${NC} - HTTP $HTTP_CODE"
        echo -e "  ${RED}Response:${NC} $BODY"
    fi

    echo ""
}

echo "Configuration:"
echo "  Keycloak URL: $KEYCLOAK_URL"
echo "  Realm: $REALM"
echo "  Client ID: $CLIENT_ID"
echo "  Password: $DEFAULT_PASSWORD"
echo ""
echo -e "${BLUE}========================================${NC}"
echo ""

# Test all users
echo -e "${BLUE}Testing Full Administrators:${NC}"
test_user_auth "admin@pragma.com.co" "System Administrator"
test_user_auth "laura.diaz@pragma.com.co" "Laura Díaz"

echo -e "${BLUE}Testing Knowledge Managers:${NC}"
test_user_auth "carlos.rodriguez@pragma.com.co" "Carlos Rodríguez"
test_user_auth "sofia.gonzalez@pragma.com.co" "Sofía González"

echo -e "${BLUE}Testing Project/Account Managers:${NC}"
test_user_auth "juan.perez@pragma.com.co" "Juan Pérez"
test_user_auth "maria.garcia@pragma.com.co" "María García"

echo -e "${BLUE}Testing People Managers:${NC}"
test_user_auth "jorge.sanchez@pragma.com.co" "Jorge Sánchez"
test_user_auth "paula.ramirez@pragma.com.co" "Paula Ramírez"

echo -e "${BLUE}Testing Regular Users:${NC}"
test_user_auth "luis.lopez@pragma.com.co" "Luis López"
test_user_auth "diego.hernandez@pragma.com.co" "Diego Hernández"
test_user_auth "camila.flores@pragma.com.co" "Camila Flores"
test_user_auth "santiago.parra@pragma.com.co" "Santiago Parra"
test_user_auth "natalia.moreno@pragma.com.co" "Natalia Moreno"
test_user_auth "andres.torres@pragma.com.co" "Andrés Torres"

echo -e "${BLUE}Testing Inactive User (should fail):${NC}"
test_user_auth "ana.martinez@pragma.com.co" "Ana Martínez (Inactive)"

echo -e "${BLUE}========================================${NC}"
echo -e "${GREEN}Authentication testing completed!${NC}"
echo -e "${BLUE}========================================${NC}"