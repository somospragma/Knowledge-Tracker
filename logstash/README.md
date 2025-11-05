# Logstash Configuration for Knowledge Tracker

This directory contains Logstash configuration files that sync data from PostgreSQL to Elasticsearch for search and analytics capabilities.

## Directory Structure

```
logstash/
├── config/
│   ├── logstash.yml          # Main Logstash configuration
│   └── pipelines.yml         # Pipeline definitions
└── pipeline/
    ├── accounts-pipeline.conf
    ├── projects-pipeline.conf
    ├── pragmatics-pipeline.conf
    ├── knowledge-pipeline.conf
    └── applied-knowledge-pipeline.conf
```

## Pipelines Overview

### 1. Accounts Pipeline (`accounts-sync`)
**Purpose**: Syncs client account data with territory information.

**Features**:
- Runs every 30 seconds
- Includes territory name via JOIN
- Calculates total projects per account
- Creates monthly indices: `accounts-YYYY.MM`

**Fields**:
- Account details (id, name, status)
- Territory information (territory_id, territory_name)
- Aggregated data (total_projects)
- Metadata (created_at, updated_at, sync_timestamp)

---

### 2. Projects Pipeline (`projects-sync`)
**Purpose**: Syncs project data with account, territory, and staffing information.

**Features**:
- Runs every 30 seconds
- Enriched with account and territory data
- Calculates project duration and active status
- Counts assigned pragmatics and knowledge applied
- Creates monthly indices: `projects-YYYY.MM`

**Calculated Fields**:
- `duration_days`: Project duration from start to end date
- `is_currently_active`: Boolean flag for active projects
- `total_pragmatics`: Number of staff assigned
- `total_knowledge_applied`: Distinct knowledge count

---

### 3. Pragmatics Pipeline (`pragmatics-sync`)
**Purpose**: Syncs employee (Pragmatic) data with organizational context.

**Features**:
- Runs every 30 seconds
- Includes chapter and KC-Team information
- Aggregates knowledge applications
- Creates full name field
- Creates monthly indices: `pragmatics-YYYY.MM`

**Enriched Data**:
- Employee details (id, email, first_name, last_name, status)
- Organizational structure (chapter_name, kc_team_name)
- Knowledge portfolio (total_knowledge_applied, knowledge_names array)
- Project involvement (total_projects)

---

### 4. Knowledge Pipeline (`knowledge-sync`)
**Purpose**: Syncs knowledge catalog with usage statistics.

**Features**:
- Runs every 60 seconds (less frequent - catalog changes slowly)
- Includes category information
- Calculates popularity metrics
- Creates single index: `knowledge` (no time-based indexing)

**Calculated Fields**:
- `times_applied`: Number of times this knowledge was applied
- `projects_using`: Number of distinct projects using it
- `popularity_score`: Weighted score (times_applied * 2 + projects_using * 5)
- `popularity_tier`: Classification (high, medium, low, unused)
- `project_names`: Array of project names using this knowledge

---

### 5. Applied Knowledge Pipeline (`applied-knowledge-sync`) ⭐ CORE
**Purpose**: Syncs knowledge application assignments - the CORE business entity that connects projects, pragmatics, and knowledge.

**Features**:
- Runs every 30 seconds
- Most comprehensive pipeline with data from all entities
- Calculates assignment duration and status
- Creates monthly indices: `applied-knowledge-YYYY.MM`

**Enriched Data**:
- Project context (name, status, account, territory)
- Pragmatic context (full name, email, chapter, KC-Team)
- Knowledge context (name, description, category)
- Knowledge level details (name, attributes)
- Assignment dates (onboard_date, offboard_date)

**Calculated Fields**:
- `pragmatic_full_name`: Concatenated first and last name
- `assignment_duration_days`: Days from onboard to offboard (or today)
- `is_currently_assigned`: Boolean flag for active assignments
- `assignment_status`: Classification (active, completed, inactive)

---

## Configuration Files

### `logstash.yml`
Main Logstash configuration with:
- HTTP API settings (port 9600)
- Pipeline workers and batch size
- Queue configuration
- Log level settings

### `pipelines.yml`
Defines all 5 pipelines with:
- Pipeline IDs
- Configuration file paths
- Worker allocation per pipeline

---

## Monitoring

### Health Check
```bash
curl http://localhost:9600/_node/stats?pretty
```

### Pipeline Statistics
```bash
# All pipelines
curl http://localhost:9600/_node/stats/pipelines?pretty

# Specific pipeline
curl http://localhost:9600/_node/stats/pipelines/accounts-sync?pretty
```

### Check Pipeline Progress
```bash
# View last run metadata (inside container)
docker exec -it knowledge-tracker-logstash-dev cat /usr/share/logstash/data/.logstash_jdbc_last_run_accounts
```

---

## Elasticsearch Indices

### List All Indices
```bash
curl http://localhost:9200/_cat/indices?v
```

### Search Examples

**Search Accounts**:
```bash
curl http://localhost:9200/accounts-*/_search?pretty
```

**Search Projects by Status**:
```bash
curl -X GET "http://localhost:9200/projects-*/_search?pretty" -H 'Content-Type: application/json' -d'
{
  "query": {
    "term": { "status": "Active" }
  }
}
'
```

**Search Pragmatics by Chapter**:
```bash
curl -X GET "http://localhost:9200/pragmatics-*/_search?pretty" -H 'Content-Type: application/json' -d'
{
  "query": {
    "match": { "chapter_name": "Backend" }
  }
}
'
```

**Find Popular Knowledge**:
```bash
curl -X GET "http://localhost:9200/knowledge/_search?pretty" -H 'Content-Type: application/json' -d'
{
  "query": {
    "term": { "popularity_tier": "high" }
  },
  "sort": [
    { "popularity_score": "desc" }
  ]
}
'
```

**Search Active Assignments**:
```bash
curl -X GET "http://localhost:9200/applied-knowledge-*/_search?pretty" -H 'Content-Type: application/json' -d'
{
  "query": {
    "bool": {
      "must": [
        { "term": { "is_currently_assigned": true } }
      ]
    }
  }
}
'
```

---

## Customization

### Changing Sync Frequency

Edit the pipeline configuration file and modify the `schedule` parameter:

```conf
# Every 30 seconds (default)
schedule => "*/30 * * * * *"

# Every 5 minutes
schedule => "*/5 * * * *"

# Every hour
schedule => "0 * * * *"
```

### Adding Custom Fields

Add filters in the pipeline configuration:

```conf
filter {
  mutate {
    add_field => {
      "custom_field" => "custom_value"
    }
  }
}
```

### Modifying SQL Queries

Edit the `statement` parameter in the JDBC input to add/remove fields or change JOINs.

---

## Troubleshooting

### Pipeline Not Running
1. Check Logstash logs: `docker-compose logs -f logstash`
2. Verify PostgreSQL connection
3. Check pipeline configuration syntax

### Data Not Syncing
1. Verify the tracking column values
2. Check if data was updated in PostgreSQL after last sync
3. Review Logstash logs for errors

### Performance Issues
1. Adjust heap size in `.env`: `LOGSTASH_HEAP_SIZE=512m`
2. Modify pipeline workers in `pipelines.yml`
3. Adjust batch size in `logstash.yml`

### Reset Pipeline Tracking
```bash
# Remove last run metadata (forces full resync)
docker exec -it knowledge-tracker-logstash-dev rm /usr/share/logstash/data/.logstash_jdbc_last_run_*
docker-compose restart logstash
```

---

## Important Notes

- **PostgreSQL is the source of truth** - All writes should go to PostgreSQL
- Elasticsearch is read-only from the application perspective
- Logstash uses incremental sync based on `updated_at` timestamps
- Monthly indices help with data retention and performance
- The JDBC driver is included in the Logstash Docker image

---

## Further Reading

- [Logstash JDBC Input Plugin](https://www.elastic.co/guide/en/logstash/current/plugins-inputs-jdbc.html)
- [Logstash Filter Plugins](https://www.elastic.co/guide/en/logstash/current/filter-plugins.html)
- [Elasticsearch Query DSL](https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl.html)
- [Kibana Visualization](https://www.elastic.co/guide/en/kibana/current/dashboard.html)
