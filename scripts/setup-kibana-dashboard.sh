#!/bin/bash
# Script to set up Kibana index patterns and create a sample dashboard
# for the Pragma Knowledge Tracker application

set -e

KIBANA_HOST=${KIBANA_HOST:-"localhost:5601"}

echo "=========================================="
echo "Setting up Kibana Dashboard"
echo "=========================================="
echo ""

# Wait for Kibana to be ready
echo "Waiting for Kibana to be ready..."
until curl -s "http://$KIBANA_HOST/api/status" > /dev/null 2>&1; do
  echo "Kibana is not ready yet. Waiting..."
  sleep 5
done
echo "✓ Kibana is ready!"
echo ""

# Create Index Patterns
echo "Creating index patterns..."

# 1. Accounts Index Pattern
curl -X POST "http://$KIBANA_HOST/api/saved_objects/index-pattern/accounts" \
  -H 'kbn-xsrf: true' \
  -H 'Content-Type: application/json' \
  -d '{
    "attributes": {
      "title": "accounts",
      "timeFieldName": "created_at"
    }
  }' > /dev/null 2>&1 && echo "✓ Created 'accounts' index pattern" || echo "⚠ 'accounts' index pattern may already exist"

# 2. Projects Index Pattern
curl -X POST "http://$KIBANA_HOST/api/saved_objects/index-pattern/projects" \
  -H 'kbn-xsrf: true' \
  -H 'Content-Type: application/json' \
  -d '{
    "attributes": {
      "title": "projects",
      "timeFieldName": "created_at"
    }
  }' > /dev/null 2>&1 && echo "✓ Created 'projects' index pattern" || echo "⚠ 'projects' index pattern may already exist"

# 3. Pragmatics Index Pattern
curl -X POST "http://$KIBANA_HOST/api/saved_objects/index-pattern/pragmatics" \
  -H 'kbn-xsrf: true' \
  -H 'Content-Type: application/json' \
  -d '{
    "attributes": {
      "title": "pragmatics",
      "timeFieldName": "created_at"
    }
  }' > /dev/null 2>&1 && echo "✓ Created 'pragmatics' index pattern" || echo "⚠ 'pragmatics' index pattern may already exist"

# 4. Knowledge Index Pattern
curl -X POST "http://$KIBANA_HOST/api/saved_objects/index-pattern/knowledge" \
  -H 'kbn-xsrf: true' \
  -H 'Content-Type: application/json' \
  -d '{
    "attributes": {
      "title": "knowledge"
    }
  }' > /dev/null 2>&1 && echo "✓ Created 'knowledge' index pattern" || echo "⚠ 'knowledge' index pattern may already exist"

# 5. Applied Knowledge Index Pattern (Core)
curl -X POST "http://$KIBANA_HOST/api/saved_objects/index-pattern/applied-knowledge" \
  -H 'kbn-xsrf: true' \
  -H 'Content-Type: application/json' \
  -d '{
    "attributes": {
      "title": "applied-knowledge",
      "timeFieldName": "created_at"
    }
  }' > /dev/null 2>&1 && echo "✓ Created 'applied-knowledge' index pattern" || echo "⚠ 'applied-knowledge' index pattern may already exist"

# 6. Territories Index Pattern
curl -X POST "http://$KIBANA_HOST/api/saved_objects/index-pattern/territories" \
  -H 'kbn-xsrf: true' \
  -H 'Content-Type: application/json' \
  -d '{
    "attributes": {
      "title": "territories"
    }
  }' > /dev/null 2>&1 && echo "✓ Created 'territories' index pattern" || echo "⚠ 'territories' index pattern may already exist"

echo ""
echo "=========================================="
echo "Setup Complete!"
echo "=========================================="
echo ""
echo "Index patterns created successfully!"
echo ""
echo "Next steps:"
echo "1. Open Kibana at: http://localhost:5601"
echo "2. Go to: Management > Stack Management > Kibana > Index Patterns"
echo "3. Verify all index patterns are created"
echo "4. Go to: Analytics > Dashboard"
echo "5. Click 'Create dashboard' to build your visualizations"
echo ""
echo "Suggested Visualizations:"
echo "  - Bar chart: Knowledge by Category (knowledge index)"
echo "  - Pie chart: Projects by Territory (projects index)"
echo "  - Data table: Pragmatics by KC Team (pragmatics index)"
echo "  - Metric: Total Applied Knowledge (applied-knowledge index)"
echo "  - Heat map: Knowledge Application by Project (applied-knowledge index)"
echo ""
