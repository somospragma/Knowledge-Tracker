# Kibana Dashboard Setup Guide

This guide explains how to visualize your Knowledge Tracker data using Kibana dashboards.

## Overview

The Knowledge Tracker application syncs PostgreSQL data to Elasticsearch, which can then be visualized in Kibana. This provides powerful analytics and reporting capabilities for tracking knowledge application across projects and employees.

## Quick Start

### 1. Start the Development Environment

```bash
./start-dev.sh --with-all
```

This starts:
- PostgreSQL (database)
- Elasticsearch (search & analytics engine)
- Kibana (visualization tool)
- Logstash (data pipeline - optional)
- pgAdmin (database management - optional)

### 2. Load Sample Data

```bash
# Data is automatically loaded on first startup from:
# src/main/resources/sql/data-dev.sql
```

### 3. Sync Data to Elasticsearch

```bash
./scripts/sync-postgres-to-es.sh
```

This script exports data from PostgreSQL and indexes it in Elasticsearch.

### 4. Create Kibana Index Patterns

```bash
./scripts/setup-kibana-dashboard.sh
```

This creates index patterns for all your data sources.

### 5. Access Kibana

Open your browser and go to: **http://localhost:5601**

---

## Available Data Indices

After running the sync script, you'll have the following indices in Elasticsearch:

| Index Name | Description | Document Count (Sample) | Key Fields |
|------------|-------------|------------------------|------------|
| `accounts` | Client organizations | 8 | name, territory_name, status |
| `projects` | Client projects | 11 | name, account_name, status, start_date, end_date |
| `pragmatics` | Pragma employees | 21 | full_name, email, chapter_name, kc_team_name, status |
| `knowledge` | Technical skills/knowledge | 50 | name, category_name, description, approved_status |
| `applied-knowledge` | Knowledge applications (CORE) | 50 | pragmatic_name, project_name, knowledge_name, knowledge_level_name |
| `territories` | Geographic regions | 6 | name |

---

## Creating Your First Dashboard

### Step 1: Open Kibana

Navigate to: **http://localhost:5601**

### Step 2: Verify Index Patterns

1. Click the menu icon (☰) in the top-left
2. Go to: **Management** → **Stack Management**
3. Under **Kibana**, click **Index Patterns**
4. Verify you see all 6 index patterns

### Step 3: Create a Dashboard

1. Click the menu icon (☰)
2. Go to: **Analytics** → **Dashboard**
3. Click **Create dashboard**
4. Click **Create visualization**

### Step 4: Add Visualizations

Here are some suggested visualizations to add:

#### Visualization 1: Knowledge by Category (Bar Chart)

**What it shows:** Distribution of technical knowledge across categories

1. Click **Create visualization**
2. Select index pattern: **`knowledge`**
3. Chart type: **Bar vertical**
4. Configuration:
   - **Vertical axis (Metrics):**
     - Aggregation: `Count`
     - Custom label: `Number of Knowledge Items`
   - **Horizontal axis (Buckets):**
     - Aggregation: `Terms`
     - Field: `category_name.keyword`
     - Order by: `Metric: Count`
     - Size: `10`
     - Custom label: `Knowledge Category`
5. Click **Save and return**

#### Visualization 2: Projects by Territory (Pie Chart)

**What it shows:** Geographic distribution of projects

1. Click **Create visualization**
2. Select index pattern: **`projects`**
3. Chart type: **Pie**
4. Configuration:
   - **Slice by (Buckets):**
     - Aggregation: `Terms`
     - Field: `territory_name.keyword`
     - Order by: `Metric: Count`
     - Size: `10`
   - **Size by (Metrics):**
     - Aggregation: `Count`
     - Custom label: `Projects`
5. Click **Save and return**

#### Visualization 3: Pragmatics by KC Team (Data Table)

**What it shows:** List of employees grouped by Knowledge Center team

1. Click **Create visualization**
2. Select index pattern: **`pragmatics`**
3. Chart type: **Table**
4. Configuration:
   - **Rows (Buckets):**
     - Aggregation: `Terms`
     - Field: `kc_team_name.keyword`
     - Order by: `Metric: Count`
     - Custom label: `KC Team`
   - **Metrics:**
     - Column 1: Count (Custom label: `Number of Pragmatics`)
5. Click **Save and return**

#### Visualization 4: Total Knowledge Applications (Metric)

**What it shows:** Total count of knowledge applied to projects

1. Click **Create visualization**
2. Select index pattern: **`applied-knowledge`**
3. Chart type: **Metric**
4. Configuration:
   - **Metric:**
     - Aggregation: `Count`
     - Custom label: `Total Knowledge Applications`
5. Click **Save and return**

#### Visualization 5: Knowledge Application Heat Map

**What it shows:** Which knowledge is most applied across projects

1. Click **Create visualization**
2. Select index pattern: **`applied-knowledge`**
3. Chart type: **Heat map**
4. Configuration:
   - **Rows (Y-axis):**
     - Aggregation: `Terms`
     - Field: `knowledge_name.keyword`
     - Size: `15`
   - **Columns (X-axis):**
     - Aggregation: `Terms`
     - Field: `project_name.keyword`
     - Size: `10`
   - **Cell color (Metrics):**
     - Aggregation: `Count`
5. Click **Save and return**

#### Visualization 6: Project Status Distribution (Donut Chart)

**What it shows:** Active vs inactive projects

1. Click **Create visualization**
2. Select index pattern: **`projects`**
3. Chart type: **Donut**
4. Configuration:
   - **Slice by:**
     - Aggregation: `Terms`
     - Field: `status.keyword`
   - **Size by:**
     - Aggregation: `Count`
5. Click **Save and return**

#### Visualization 7: Top Knowledge Skills (Bar Horizontal)

**What it shows:** Most frequently applied technical skills

1. Click **Create visualization**
2. Select index pattern: **`applied-knowledge`**
3. Chart type: **Bar horizontal**
4. Configuration:
   - **Vertical axis:**
     - Aggregation: `Terms`
     - Field: `knowledge_name.keyword`
     - Size: `10`
     - Order: `Descending`
   - **Horizontal axis:**
     - Aggregation: `Count`
     - Custom label: `Times Applied`
5. Click **Save and return**

#### Visualization 8: Knowledge by Proficiency Level (Stacked Bar)

**What it shows:** Distribution of knowledge proficiency levels

1. Click **Create visualization**
2. Select index pattern: **`applied-knowledge`**
3. Chart type: **Bar vertical stacked**
4. Configuration:
   - **Vertical axis:**
     - Aggregation: `Count`
   - **Horizontal axis:**
     - Aggregation: `Terms`
     - Field: `knowledge_category.keyword`
   - **Break down by (Legend):**
     - Aggregation: `Terms`
     - Field: `knowledge_level_name.keyword`
5. Click **Save and return**

---

## Sample Dashboard Layout

After creating all visualizations, arrange them on your dashboard:

```
┌──────────────────────────────────────────────────────────┐
│  Total Knowledge Applications: 50                        │
│  (Large Metric)                                          │
├────────────────────────────┬────────────────────────────┤
│  Knowledge by Category     │  Projects by Territory      │
│  (Bar Chart)              │  (Pie Chart)               │
├────────────────────────────┼────────────────────────────┤
│  Top Knowledge Skills      │  Knowledge by Proficiency   │
│  (Horizontal Bar)         │  (Stacked Bar)             │
├────────────────────────────┴────────────────────────────┤
│  Knowledge Application Heat Map                          │
│  (Projects × Knowledge Items)                            │
├──────────────────────────────────────────────────────────┤
│  Pragmatics by KC Team (Data Table)                      │
└──────────────────────────────────────────────────────────┘
```

---

## Saving Your Dashboard

1. Click **Save** in the top-right corner
2. Give it a name: `Pragma Knowledge Tracker - Overview`
3. Add description: `Main dashboard showing knowledge application across projects and employees`
4. Click **Save**

---

## Advanced: Time-Series Analysis

For time-based analysis (trends over time):

1. Create a new dashboard
2. Add a **Date histogram** visualization:
   - Index: `applied-knowledge`
   - Chart: **Area chart**
   - X-axis: Date histogram on `created_at`
   - Interval: `Monthly`
   - Y-axis: Count
3. This shows knowledge application trends over time

---

## Refreshing Data

Whenever you add new data to PostgreSQL, sync it to Elasticsearch:

```bash
./scripts/sync-postgres-to-es.sh
```

Then refresh your Kibana dashboard (browser refresh or auto-refresh).

---

## Troubleshooting

### No data in Kibana

1. Check Elasticsearch indices:
   ```bash
   curl "http://localhost:9200/_cat/indices?v"
   ```

2. Verify data count:
   ```bash
   curl "http://localhost:9200/applied-knowledge/_count"
   ```

3. Re-run sync script:
   ```bash
   ./scripts/sync-postgres-to-es.sh
   ```

### Index pattern not found

Re-run the setup script:
```bash
./scripts/setup-kibana-dashboard.sh
```

### Kibana not accessible

1. Check if container is running:
   ```bash
   docker ps | grep kibana
   ```

2. Check logs:
   ```bash
   docker-compose logs kibana
   ```

3. Restart Kibana:
   ```bash
   docker-compose restart kibana
   ```

---

## Next Steps

1. **Customize visualizations** - Modify colors, labels, and filters
2. **Add filters** - Create dashboard-level filters (e.g., by territory, by status)
3. **Set up alerts** - Configure Elasticsearch alerts for specific conditions
4. **Export dashboards** - Save dashboard configuration for backup/sharing
5. **Integrate with Spring Boot** - Use Elasticsearch Java client in your application

---

## Useful Kibana Features

### Filters

Add filters to focus on specific data:
- Click **Add filter** at the top
- Select field, operator, and value
- Example: `status.keyword` `is` `Active`

### Time Range

For time-based indices (accounts, projects, pragmatics, applied-knowledge):
- Use the time picker in top-right
- Select: Last 7 days, Last 30 days, Year to date, etc.

### Search

Use the search bar to find specific records:
- `project_name: "Bancolombia"`
- `kc_team_name: "KC Ciencias de la Computación"`
- `knowledge_name: "Java"`

### Drill-Down

Click on any visualization element to:
- Filter the entire dashboard
- View detailed records
- Create ad-hoc visualizations

---

## Resources

- **Kibana Documentation:** https://www.elastic.co/guide/en/kibana/current/index.html
- **Elasticsearch Query DSL:** https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl.html
- **Kibana URL:** http://localhost:5601
- **Elasticsearch URL:** http://localhost:9200

---

## Maintenance

### Daily
- Monitor dashboard performance
- Check for data sync issues

### Weekly
- Review and optimize slow queries
- Clean up unused visualizations

### Monthly
- Archive old data (if needed)
- Update visualizations based on feedback
- Review Elasticsearch index sizes
