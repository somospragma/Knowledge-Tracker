#!/bin/bash
# Start Kibana Dashboard for Knowledge Tracker
# This script starts the ELK stack with Kibana and verifies data is available

set -e

echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "  Knowledge Tracker - Kibana Dashboard Startup"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

# Change to project root directory
cd "$(dirname "$0")/.."

echo "📦 Step 1: Starting ELK Stack with Kibana..."
echo "   This may take 2-3 minutes for first startup"
echo ""

docker-compose --profile with-kibana up -d

echo ""
echo "⏳ Step 2: Waiting for services to be healthy..."
echo ""

# Wait for Elasticsearch
echo "   Waiting for Elasticsearch..."
until curl -s http://localhost:9200/_cluster/health >/dev/null 2>&1; do
    echo "      Elasticsearch is starting..."
    sleep 5
done
echo "   ✓ Elasticsearch is ready"

# Wait for Logstash
echo "   Waiting for Logstash..."
until curl -s http://localhost:9600/_node/stats >/dev/null 2>&1; do
    echo "      Logstash is starting..."
    sleep 5
done
echo "   ✓ Logstash is ready"

# Wait for Kibana
echo "   Waiting for Kibana (this takes longest - ~60-90 seconds)..."
KIBANA_WAIT=0
until curl -s http://localhost:5601/api/status >/dev/null 2>&1; do
    KIBANA_WAIT=$((KIBANA_WAIT + 5))
    if [ $KIBANA_WAIT -gt 180 ]; then
        echo "   ⚠ Kibana is taking longer than expected. Check logs with:"
        echo "      docker logs knowledge-tracker-kibana-dev"
        exit 1
    fi
    echo "      Kibana is starting... (${KIBANA_WAIT}s elapsed)"
    sleep 5
done
echo "   ✓ Kibana is ready"

echo ""
echo "🔍 Step 3: Checking for data..."
echo ""

# Wait a bit for Logstash to sync initial data
sleep 10

# Check if we have data
INDEX_COUNT=$(curl -s "http://localhost:9200/applied-knowledge-*/_count" | grep -o '"count":[0-9]*' | grep -o '[0-9]*' || echo "0")

if [ "$INDEX_COUNT" -gt 0 ]; then
    echo "   ✓ Found $INDEX_COUNT documents in Elasticsearch"
else
    echo "   ⚠ No data found yet in Elasticsearch"
    echo "   Logstash is syncing data from PostgreSQL..."
    echo "   This should complete within 30-60 seconds"
    echo ""
    echo "   You can monitor Logstash logs with:"
    echo "   docker logs -f knowledge-tracker-logstash-dev"
fi

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "  ✅ Kibana is Ready!"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "🌐 Access Kibana at: http://localhost:5601"
echo ""
echo "📚 Next Steps:"
echo "   1. Open the setup guide:"
echo "      docs/kibana-dashboard-setup.md"
echo ""
echo "   2. Create your index pattern:"
echo "      - Go to Stack Management → Index Patterns"
echo "      - Create pattern: applied-knowledge-*"
echo "      - Use time field: updated_at"
echo ""
echo "   3. Test queries in Dev Tools:"
echo "      - Go to Dev Tools (≡ menu → Dev Tools)"
echo "      - Try queries from: docs/elasticsearch-test-queries.md"
echo ""
echo "   4. Create visualizations and dashboard"
echo "      - Follow the step-by-step guide in the docs"
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "💡 Useful Commands:"
echo "   View logs:        docker-compose logs -f kibana"
echo "   Stop services:    docker-compose --profile with-kibana down"
echo "   Restart services: docker-compose --profile with-kibana restart"
echo ""
echo "🔍 Quick Data Check:"
echo "   curl http://localhost:9200/applied-knowledge-*/_count"
echo ""