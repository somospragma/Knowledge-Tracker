#!/bin/bash

###############################################################################
# Keycloak User Seeding Script
# Creates dummy users in Keycloak with appropriate roles
# Matches the users defined in data-dev.sql and data-test.sql
###############################################################################

set -e  # Exit on error

# Configuration
KEYCLOAK_URL="${KEYCLOAK_URL:-http://localhost:8180}"
REALM="${KEYCLOAK_REALM:-pragma-knowledge-tracker}"
ADMIN_USER="${KEYCLOAK_ADMIN_USER:-admin}"
ADMIN_PASSWORD="${KEYCLOAK_ADMIN_PASSWORD:-admin}"
DEFAULT_PASSWORD="${DEFAULT_USER_PASSWORD:-Pragma2024!}"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}Keycloak User Seeding Script${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# Function to print colored messages
print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if curl is installed
if ! command -v curl &> /dev/null; then
    print_error "curl is required but not installed. Please install curl and try again."
    exit 1
fi

# Check if jq is installed
if ! command -v jq &> /dev/null; then
    print_warning "jq is not installed. JSON output will not be formatted."
    JQ_AVAILABLE=false
else
    JQ_AVAILABLE=true
fi

# Wait for Keycloak to be ready
print_info "Checking Keycloak availability at ${KEYCLOAK_URL}..."
MAX_RETRIES=30
RETRY_COUNT=0

while [ $RETRY_COUNT -lt $MAX_RETRIES ]; do
    # Check if realm endpoint is accessible (more reliable than health endpoint)
    if curl -s -f "${KEYCLOAK_URL}/realms/${REALM}" > /dev/null 2>&1; then
        print_success "Keycloak is ready!"
        break
    fi

    RETRY_COUNT=$((RETRY_COUNT + 1))
    if [ $RETRY_COUNT -eq $MAX_RETRIES ]; then
        print_error "Keycloak is not available after ${MAX_RETRIES} attempts. Exiting."
        exit 1
    fi

    print_info "Waiting for Keycloak... (Attempt $RETRY_COUNT/$MAX_RETRIES)"
    sleep 2
done

echo ""

# Get admin access token
print_info "Authenticating as admin user..."
TOKEN_RESPONSE=$(curl -s -X POST "${KEYCLOAK_URL}/realms/master/protocol/openid-connect/token" \
    -H "Content-Type: application/x-www-form-urlencoded" \
    -d "username=${ADMIN_USER}" \
    -d "password=${ADMIN_PASSWORD}" \
    -d "grant_type=password" \
    -d "client_id=admin-cli")

if [ $JQ_AVAILABLE = true ]; then
    ACCESS_TOKEN=$(echo "$TOKEN_RESPONSE" | jq -r '.access_token')
else
    ACCESS_TOKEN=$(echo "$TOKEN_RESPONSE" | grep -o '"access_token":"[^"]*' | cut -d'"' -f4)
fi

if [ -z "$ACCESS_TOKEN" ] || [ "$ACCESS_TOKEN" = "null" ]; then
    print_error "Failed to obtain admin access token. Check admin credentials."
    exit 1
fi

print_success "Successfully authenticated as admin"
echo ""

# Function to get role ID by role name
get_role_id() {
    local role_name=$1
    local response=$(curl -s -X GET "${KEYCLOAK_URL}/admin/realms/${REALM}/roles/${role_name}" \
        -H "Authorization: Bearer ${ACCESS_TOKEN}")

    if [ $JQ_AVAILABLE = true ]; then
        echo "$response" | jq -r '.id'
    else
        echo "$response" | grep -o '"id":"[^"]*' | head -1 | cut -d'"' -f4
    fi
}

# Function to create a user in Keycloak
create_user() {
    local email=$1
    local first_name=$2
    local last_name=$3
    local role=$4
    local enabled=${5:-true}

    print_info "Creating user: $email ($role)..."

    # Create user JSON payload
    local user_payload=$(cat <<EOF
{
    "username": "$email",
    "email": "$email",
    "firstName": "$first_name",
    "lastName": "$last_name",
    "enabled": $enabled,
    "emailVerified": true,
    "credentials": [{
        "type": "password",
        "value": "$DEFAULT_PASSWORD",
        "temporary": false
    }]
}
EOF
)

    # Create the user
    local create_response=$(curl -s -w "\n%{http_code}" -X POST "${KEYCLOAK_URL}/admin/realms/${REALM}/users" \
        -H "Authorization: Bearer ${ACCESS_TOKEN}" \
        -H "Content-Type: application/json" \
        -d "$user_payload")

    local http_code=$(echo "$create_response" | tail -n1)

    if [ "$http_code" -eq 201 ]; then
        print_success "User created: $email"

        # Get user ID
        local user_id_response=$(curl -s -X GET "${KEYCLOAK_URL}/admin/realms/${REALM}/users?username=$email&exact=true" \
            -H "Authorization: Bearer ${ACCESS_TOKEN}")

        if [ $JQ_AVAILABLE = true ]; then
            local user_id=$(echo "$user_id_response" | jq -r '.[0].id')
        else
            local user_id=$(echo "$user_id_response" | grep -o '"id":"[^"]*' | head -1 | cut -d'"' -f4)
        fi

        # Assign role to user
        if [ ! -z "$user_id" ] && [ "$user_id" != "null" ]; then
            local role_id=$(get_role_id "$role")

            if [ ! -z "$role_id" ] && [ "$role_id" != "null" ]; then
                local role_payload=$(cat <<EOF
[{
    "id": "$role_id",
    "name": "$role"
}]
EOF
)

                curl -s -X POST "${KEYCLOAK_URL}/admin/realms/${REALM}/users/${user_id}/role-mappings/realm" \
                    -H "Authorization: Bearer ${ACCESS_TOKEN}" \
                    -H "Content-Type: application/json" \
                    -d "$role_payload" > /dev/null

                print_success "Assigned role '$role' to user $email"
            else
                print_warning "Could not find role '$role' for user $email"
            fi
        fi
    elif [ "$http_code" -eq 409 ]; then
        print_warning "User already exists: $email (skipping)"
    else
        print_error "Failed to create user $email (HTTP $http_code)"
    fi

    echo ""
}

# Create users
print_info "Creating users in Keycloak realm: ${REALM}"
echo ""

# Full Administrators
create_user "admin@pragma.com.co" "System" "Administrator" "full_administrator" true
create_user "laura.diaz@pragma.com.co" "Laura" "Díaz" "full_administrator" true

# Knowledge Managers
create_user "carlos.rodriguez@pragma.com.co" "Carlos" "Rodríguez" "knowledge_manager" true
create_user "sofia.gonzalez@pragma.com.co" "Sofía" "González" "knowledge_manager" true

# Project/Account Managers
create_user "juan.perez@pragma.com.co" "Juan" "Pérez" "project_account_manager" true
create_user "maria.garcia@pragma.com.co" "María" "García" "project_account_manager" true

# People Managers
create_user "jorge.sanchez@pragma.com.co" "Jorge" "Sánchez" "people_manager" true
create_user "paula.ramirez@pragma.com.co" "Paula" "Ramírez" "people_manager" true

# Regular Users
create_user "luis.lopez@pragma.com.co" "Luis" "López" "user" true
create_user "diego.hernandez@pragma.com.co" "Diego" "Hernández" "user" true
create_user "camila.flores@pragma.com.co" "Camila" "Flores" "user" true
create_user "santiago.parra@pragma.com.co" "Santiago" "Parra" "user" true
create_user "natalia.moreno@pragma.com.co" "Natalia" "Moreno" "user" true
create_user "andres.torres@pragma.com.co" "Andrés" "Torres" "user" true

# Inactive User (for testing)
create_user "ana.martinez@pragma.com.co" "Ana" "Martínez" "user" false

echo ""
print_success "User seeding completed!"
echo ""
echo -e "${BLUE}========================================${NC}"
echo -e "${GREEN}Default Password for all users:${NC} ${DEFAULT_PASSWORD}"
echo -e "${BLUE}========================================${NC}"
echo ""
echo "You can now login to Keycloak Admin Console at:"
echo "${KEYCLOAK_URL}"
echo ""
echo "Or test authentication with:"
echo "curl -X POST ${KEYCLOAK_URL}/realms/${REALM}/protocol/openid-connect/token \\"
echo "  -H \"Content-Type: application/x-www-form-urlencoded\" \\"
echo "  -d \"grant_type=password\" \\"
echo "  -d \"client_id=knowledge-tracker-api\" \\"
echo "  -d \"client_secret=YOUR_CLIENT_SECRET\" \\"
echo "  -d \"username=admin@pragma.com.co\" \\"
echo "  -d \"password=${DEFAULT_PASSWORD}\""
echo ""
