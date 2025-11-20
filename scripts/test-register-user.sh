#!/bin/bash

###############################################################################
# RegisterUserUseCase Test Script
# Tests user registration endpoint with various scenarios
###############################################################################

# Configuration
API_URL="${API_URL:-http://localhost:8080}"
ENDPOINT="${API_URL}/api/v1/users/register"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}RegisterUserUseCase Test Script${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""
echo "Endpoint: ${ENDPOINT}"
echo ""

# Function to test user registration
test_registration() {
    local test_name=$1
    local payload=$2
    local expected_status=$3

    echo -e "${CYAN}Test: ${test_name}${NC}"
    echo -e "${BLUE}Payload:${NC}"
    echo "$payload" | jq '.' 2>/dev/null || echo "$payload"
    echo ""

    RESPONSE=$(curl -s -w "\nHTTP_CODE:%{http_code}" -X POST "${ENDPOINT}" \
        -H "Content-Type: application/json" \
        -d "$payload")

    HTTP_CODE=$(echo "$RESPONSE" | grep "HTTP_CODE" | cut -d':' -f2)
    BODY=$(echo "$RESPONSE" | grep -v "HTTP_CODE")

    echo -e "${BLUE}Response (HTTP $HTTP_CODE):${NC}"

    if command -v jq &> /dev/null; then
        echo "$BODY" | jq '.' 2>/dev/null || echo "$BODY"
    else
        echo "$BODY"
    fi

    if [ "$HTTP_CODE" = "$expected_status" ]; then
        echo -e "${GREEN}✓ PASSED${NC} - Got expected HTTP $expected_status"
    else
        echo -e "${RED}✗ FAILED${NC} - Expected HTTP $expected_status, got HTTP $HTTP_CODE"
    fi

    echo ""
    echo "---"
    echo ""
}

# Wait for application to be ready
echo -e "${BLUE}Checking if application is running...${NC}"
for i in {1..5}; do
    if curl -s -f "${API_URL}/actuator/health" > /dev/null 2>&1; then
        echo -e "${GREEN}✓ Application is ready${NC}"
        echo ""
        break
    fi
    if [ $i -eq 5 ]; then
        echo -e "${YELLOW}⚠ Warning: Application health check failed. Proceeding anyway...${NC}"
        echo ""
    else
        echo "Waiting for application... (attempt $i/5)"
        sleep 2
    fi
done

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}Running Test Cases${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# Test 1: Valid user registration - Full Administrator
test_registration \
    "1. Register Full Administrator" \
    '{
        "email": "test.admin@pragma.com.co",
        "firstName": "Test",
        "lastName": "Administrator",
        "systemRole": "FULL_ADMINISTRATOR",
        "password": "TestPassword123!",
        "temporaryPassword": false
    }' \
    "201"

# Test 2: Valid user registration - Knowledge Manager
test_registration \
    "2. Register Knowledge Manager" \
    '{
        "email": "test.km@pragma.com.co",
        "firstName": "Test",
        "lastName": "Knowledge Manager",
        "systemRole": "KNOWLEDGE_MANAGER",
        "password": "TestPassword123!",
        "temporaryPassword": false
    }' \
    "201"

# Test 3: Valid user registration - Project/Account Manager
test_registration \
    "3. Register Project/Account Manager" \
    '{
        "email": "test.pam@pragma.com.co",
        "firstName": "Test",
        "lastName": "Project Manager",
        "systemRole": "PROJECT_ACCOUNT_MANAGER",
        "password": "TestPassword123!",
        "temporaryPassword": false
    }' \
    "201"

# Test 4: Valid user registration - People Manager
test_registration \
    "4. Register People Manager" \
    '{
        "email": "test.pm@pragma.com.co",
        "firstName": "Test",
        "lastName": "People Manager",
        "systemRole": "PEOPLE_MANAGER",
        "password": "TestPassword123!",
        "temporaryPassword": false
    }' \
    "201"

# Test 5: Valid user registration - Regular User
test_registration \
    "5. Register Regular User" \
    '{
        "email": "test.user@pragma.com.co",
        "firstName": "Test",
        "lastName": "User",
        "systemRole": "USER",
        "password": "TestPassword123!",
        "temporaryPassword": false
    }' \
    "201"

# Test 6: Duplicate email (should fail with 409)
test_registration \
    "6. Duplicate Email (should fail)" \
    '{
        "email": "test.admin@pragma.com.co",
        "firstName": "Duplicate",
        "lastName": "User",
        "systemRole": "USER",
        "password": "TestPassword123!",
        "temporaryPassword": false
    }' \
    "409"

# Test 7: Invalid email format (should fail with 400)
test_registration \
    "7. Invalid Email Format (should fail)" \
    '{
        "email": "invalid-email",
        "firstName": "Invalid",
        "lastName": "Email",
        "systemRole": "USER",
        "password": "TestPassword123!",
        "temporaryPassword": false
    }' \
    "400"

# Test 8: Missing required field (should fail with 400)
test_registration \
    "8. Missing First Name (should fail)" \
    '{
        "email": "test.missing@pragma.com.co",
        "lastName": "Missing",
        "systemRole": "USER",
        "password": "TestPassword123!",
        "temporaryPassword": false
    }' \
    "400"

# Test 9: Password too short (should fail with 400)
test_registration \
    "9. Password Too Short (should fail)" \
    '{
        "email": "test.shortpass@pragma.com.co",
        "firstName": "Short",
        "lastName": "Password",
        "systemRole": "USER",
        "password": "short",
        "temporaryPassword": false
    }' \
    "400"

# Test 10: Invalid system role (should fail with 400)
test_registration \
    "10. Invalid System Role (should fail)" \
    '{
        "email": "test.invalidrole@pragma.com.co",
        "firstName": "Invalid",
        "lastName": "Role",
        "systemRole": "SUPER_ADMIN",
        "password": "TestPassword123!",
        "temporaryPassword": false
    }' \
    "400"

# Test 11: Temporary password flag
test_registration \
    "11. Register with Temporary Password" \
    '{
        "email": "test.temp@pragma.com.co",
        "firstName": "Temporary",
        "lastName": "Password",
        "systemRole": "USER",
        "password": "TempPassword123!",
        "temporaryPassword": true
    }' \
    "201"

echo -e "${BLUE}========================================${NC}"
echo -e "${GREEN}Testing Completed!${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""
echo "Summary:"
echo "- Valid registrations should return HTTP 201"
echo "- Duplicate email should return HTTP 409"
echo "- Invalid data should return HTTP 400"
echo ""
echo "To verify users were created in Keycloak:"
echo "1. Open: http://localhost:8180"
echo "2. Login: admin / admin"
echo "3. Realm: pragma-knowledge-tracker"
echo "4. Go to: Users"
echo ""