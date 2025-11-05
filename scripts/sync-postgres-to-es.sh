#!/bin/bash
# Script to manually sync PostgreSQL data to Elasticsearch for Kibana dashboards
# This bypasses Logstash scheduler issues and directly populates Elasticsearch

set -e

POSTGRES_HOST=${POSTGRES_HOST:-"localhost"}
POSTGRES_PORT=${POSTGRES_PORT:-5432}
POSTGRES_DB=${POSTGRES_DB:-"knowledge_tracker_dev"}
POSTGRES_USER=${POSTGRES_USER:-"pragma_dev"}
POSTGRES_PASSWORD=${POSTGRES_PASSWORD:-"pragma_dev_password"}
ES_HOST=${ES_HOST:-"localhost:9200"}

echo "=========================================="
echo "Syncing PostgreSQL to Elasticsearch"
echo "=========================================="

# Function to export data from PostgreSQL to JSON and index in Elasticsearch
sync_table() {
  local table_name=$1
  local index_name=$2
  local query=$3

  echo "Syncing $table_name to index: $index_name"

  # Export data as JSON from PostgreSQL
  local json_data=$(docker exec knowledge-tracker-postgres-dev psql -U $POSTGRES_USER -d $POSTGRES_DB -t -c "$query" | jq -Rs 'split("\n") | map(select(length > 0)) | map(fromjson)')

  # Check if we have data
  local count=$(echo "$json_data" | jq 'length')
  echo "Found $count records"

  if [ "$count" -gt 0 ]; then
    # Create index with proper mapping (if not exists)
    curl -s -X PUT "http://$ES_HOST/$index_name" -H 'Content-Type: application/json' -d'{
      "settings": {
        "number_of_shards": 1,
        "number_of_replicas": 0
      }
    }' > /dev/null 2>&1 || true

    # Index each document
    echo "$json_data" | jq -c '.[]' | while read -r doc; do
      local id=$(echo "$doc" | jq -r '.id')
      curl -s -X PUT "http://$ES_HOST/$index_name/_doc/$id" \
        -H 'Content-Type: application/json' \
        -d "$doc" > /dev/null
    done
    echo "✓ Synced $count records to $index_name"
  else
    echo "⚠ No data found for $table_name"
  fi
  echo ""
}

# Sync Territories
sync_table "territory" "territories" "
  SELECT row_to_json(t) FROM (
    SELECT id, name, created_at, updated_at
    FROM territory
  ) t;
"

# Sync Accounts
sync_table "account" "accounts" "
  SELECT row_to_json(t) FROM (
    SELECT
      a.id,
      a.territory_id,
      t.name as territory_name,
      a.name,
      a.status,
      a.attributes,
      a.created_at,
      a.updated_at
    FROM account a
    LEFT JOIN territory t ON a.territory_id = t.id
  ) t;
"

# Sync Projects
sync_table "project" "projects" "
  SELECT row_to_json(t) FROM (
    SELECT
      p.id,
      p.account_id,
      a.name as account_name,
      a.territory_id,
      t.name as territory_name,
      p.name,
      p.status,
      p.start_date,
      p.end_date,
      p.type,
      p.attributes,
      p.created_at,
      p.updated_at
    FROM project p
    INNER JOIN account a ON p.account_id = a.id
    LEFT JOIN territory t ON a.territory_id = t.id
  ) t;
"

# Sync Pragmatics (Employees)
sync_table "pragmatic" "pragmatics" "
  SELECT row_to_json(t) FROM (
    SELECT
      pr.id,
      pr.chapter_id,
      ch.name as chapter_name,
      ch.kc_id,
      kc.name as kc_team_name,
      pr.email,
      pr.first_name,
      pr.last_name,
      pr.first_name || ' ' || pr.last_name as full_name,
      pr.status,
      pr.attributes,
      pr.created_at,
      pr.updated_at
    FROM pragmatic pr
    INNER JOIN chapter ch ON pr.chapter_id = ch.id
    INNER JOIN \"kc-team\" kc ON ch.kc_id = kc.id
  ) t;
"

# Sync Knowledge Catalog
sync_table "knowledge" "knowledge" "
  SELECT row_to_json(t) FROM (
    SELECT
      k.id,
      k.category_id,
      kc.name as category_name,
      k.name,
      k.description,
      k.approved_status,
      k.attributes
    FROM knowledge k
    INNER JOIN knowledge_category kc ON k.category_id = kc.id
  ) t;
"

# Sync Applied Knowledge (Core business data)
sync_table "applied_knowledge" "applied-knowledge" "
  SELECT row_to_json(t) FROM (
    SELECT
      ak.id,
      ak.project_id,
      p.name as project_name,
      p.status as project_status,
      a.name as account_name,
      t.name as territory_name,
      ak.pragmatic_id,
      pr.first_name || ' ' || pr.last_name as pragmatic_name,
      pr.email as pragmatic_email,
      ch.name as chapter_name,
      kc.name as kc_team_name,
      ak.knowledge_id,
      k.name as knowledge_name,
      cat.name as knowledge_category,
      ak.knowledge_level,
      kl.name as knowledge_level_name,
      ak.onboard_date,
      ak.offboard_date,
      ak.attributes,
      ak.created_at,
      ak.updated_at
    FROM applied_knowledge ak
    INNER JOIN project p ON ak.project_id = p.id
    INNER JOIN account a ON p.account_id = a.id
    LEFT JOIN territory t ON a.territory_id = t.id
    INNER JOIN pragmatic pr ON ak.pragmatic_id = pr.id
    INNER JOIN chapter ch ON pr.chapter_id = ch.id
    INNER JOIN \"kc-team\" kc ON ch.kc_id = kc.id
    INNER JOIN knowledge k ON ak.knowledge_id = k.id
    INNER JOIN knowledge_category cat ON k.category_id = cat.id
    INNER JOIN knowledge_level kl ON ak.knowledge_level = kl.id
  ) t;
"

echo "=========================================="
echo "Sync Complete!"
echo "=========================================="
echo ""
echo "Elasticsearch indices created:"
curl -s "http://$ES_HOST/_cat/indices?v"
echo ""
echo "You can now create Kibana dashboards at: http://localhost:5601"
