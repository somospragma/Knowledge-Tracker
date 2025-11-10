# Kibana Dashboard Setup Guide
## Report: Number of Users Currently Applying Knowledge (Grouped by Projects)

This guide will help you create a Kibana dashboard to visualize how many Pragmatics (employees) are currently applying specific knowledge across different projects.

---

## Prerequisites

1. **Start the ELK Stack**
   ```bash
   # Start all services including Kibana
   docker-compose --profile with-kibana up -d

   # Verify services are running
   docker ps | grep knowledge-tracker
   ```

2. **Access Kibana**
   - URL: http://localhost:5601
   - Wait for Kibana to be fully ready (can take 1-2 minutes)

3. **Verify Data in Elasticsearch**
   ```bash
   # Check if applied-knowledge index exists
   curl -X GET "http://localhost:9200/_cat/indices/applied-knowledge-*?v"

   # Check document count
   curl -X GET "http://localhost:9200/applied-knowledge-*/_count"
   ```

---

## Step 1: Create Index Pattern in Kibana

1. **Navigate to Stack Management**
   - Open Kibana at http://localhost:5601
   - Click on the hamburger menu (☰) → **Management** → **Stack Management**

2. **Create Index Pattern**
   - Click **Index Patterns** under "Kibana"
   - Click **Create index pattern**
   - **Index pattern name**: `applied-knowledge-*`
   - Click **Next step**
   - **Time field**: Select `updated_at` or `@timestamp`
   - Click **Create index pattern**

3. **Verify Fields**
   Ensure these fields are available:
   - `pragmatic_id.keyword`
   - `pragmatic_full_name.keyword`
   - `knowledge_name.keyword`
   - `project_name.keyword`
   - `project_status.keyword`
   - `territory_name.keyword`
   - `is_currently_assigned` (boolean)
   - `assignment_status.keyword`

---

## Step 2: Test Your Data with Discover

1. **Navigate to Discover**
   - Click hamburger menu (☰) → **Analytics** → **Discover**
   - Select the `applied-knowledge-*` index pattern

2. **Add Filters for "Currently Assigned"**
   - Click **+ Add filter**
   - **Field**: `is_currently_assigned`
   - **Operator**: `is`
   - **Value**: `true`
   - Click **Save**

3. **Optional: Filter by Project Status**
   - Click **+ Add filter**
   - **Field**: `project_status.keyword`
   - **Operator**: `is`
   - **Value**: `Active`
   - Click **Save**

4. **View Sample Documents**
   - You should see current assignments with all related fields
   - Verify data looks correct before creating visualizations

---

## Step 3: Create Visualizations

### Visualization 1: Users by Knowledge (Bar Chart)

**Purpose**: Count unique Pragmatics per Knowledge type

1. **Create Visualization**
   - Click hamburger menu → **Analytics** → **Visualize Library**
   - Click **Create visualization**
   - Select **Bar vertical** (or **Bar horizontal**)

2. **Configure Data Source**
   - **Index pattern**: `applied-knowledge-*`

3. **Add Filter** (Show only current assignments)
   - Click **Add filter**
   - Field: `is_currently_assigned`, Operator: `is`, Value: `true`
   - Click **Save**

4. **Configure Metrics**
   - **Vertical axis (Metrics)**:
     - Aggregation: **Unique Count**
     - Field: `pragmatic_id.keyword`
     - Custom label: `Number of Users`

5. **Configure Buckets**
   - **Horizontal axis (Buckets)**:
     - Aggregation: **Terms**
     - Field: `knowledge_name.keyword`
     - Order by: `Unique Count of pragmatic_id.keyword` (Descending)
     - Size: `20` (top 20 knowledge types)
     - Custom label: `Knowledge`

6. **Apply and Save**
   - Click **Update** (or the play button)
   - Click **Save** → Name: `Users Currently Applying Each Knowledge`

---

### Visualization 2: Users by Knowledge and Project (Data Table)

**Purpose**: Detailed breakdown by Knowledge and Project

1. **Create Visualization**
   - Click **Create visualization**
   - Select **Table**

2. **Configure Data Source**
   - **Index pattern**: `applied-knowledge-*`

3. **Add Filter** (Show only current assignments)
   - Field: `is_currently_assigned`, Operator: `is`, Value: `true`

4. **Configure Metrics**
   - **Metrics**:
     - Aggregation: **Unique Count**
     - Field: `pragmatic_id.keyword`
     - Custom label: `Number of Users`

5. **Configure Rows** (Two-level grouping)
   - **Row 1 - Split rows**:
     - Aggregation: **Terms**
     - Field: `knowledge_name.keyword`
     - Order by: `Unique Count of pragmatic_id.keyword` (Descending)
     - Size: `50`
     - Custom label: `Knowledge`

   - **Row 2 - Split rows** (Sub-bucket):
     - Aggregation: **Terms**
     - Field: `project_name.keyword`
     - Order by: `Unique Count of pragmatic_id.keyword` (Descending)
     - Size: `100`
     - Custom label: `Project`

6. **Apply and Save**
   - Click **Update**
   - Click **Save** → Name: `Users by Knowledge and Project (Detailed)`

---

### Visualization 3: Top Projects by Knowledge (Heatmap)

**Purpose**: Visual heatmap showing which projects use which knowledge

1. **Create Visualization**
   - Click **Create visualization**
   - Select **Heat map**

2. **Configure Data Source**
   - **Index pattern**: `applied-knowledge-*`

3. **Add Filter**
   - Field: `is_currently_assigned`, Operator: `is`, Value: `true`

4. **Configure Metrics**
   - **Value (Metrics)**:
     - Aggregation: **Unique Count**
     - Field: `pragmatic_id.keyword`
     - Custom label: `Users`

5. **Configure Buckets**
   - **Y-axis (Rows)**:
     - Aggregation: **Terms**
     - Field: `knowledge_name.keyword`
     - Order by: Metric (Descending)
     - Size: `15`

   - **X-axis (Columns)**:
     - Aggregation: **Terms**
     - Field: `project_name.keyword`
     - Order by: Metric (Descending)
     - Size: `15`

6. **Apply and Save**
   - Click **Update**
   - Click **Save** → Name: `Knowledge Usage Heatmap by Project`

---

### Visualization 4: Filter by Specific Knowledge (Controls)

**Purpose**: Interactive filter to select specific knowledge

1. **Create Visualization**
   - Click **Create visualization**
   - Select **Controls**

2. **Add Control**
   - Click **Add control**
   - **Control type**: Options list
   - **Index pattern**: `applied-knowledge-*`
   - **Field**: `knowledge_name.keyword`
   - **Label**: `Select Knowledge`
   - **Parent control**: None
   - Click **Save**

3. **Optional: Add Project Filter**
   - Click **Add control**
   - **Control type**: Options list
   - **Field**: `project_name.keyword`
   - **Label**: `Select Project`
   - Click **Save**

4. **Save**
   - Click **Save** → Name: `Knowledge and Project Filters`

---

## Step 4: Create the Dashboard

1. **Create New Dashboard**
   - Click hamburger menu → **Analytics** → **Dashboard**
   - Click **Create dashboard**

2. **Add Visualizations**
   - Click **Add from library**
   - Select all visualizations you created:
     - `Knowledge and Project Filters` (add at top)
     - `Users Currently Applying Each Knowledge`
     - `Users by Knowledge and Project (Detailed)`
     - `Knowledge Usage Heatmap by Project`

3. **Arrange Layout**
   - Drag the **Filters** control to the top (full width)
   - Arrange the bar chart and table side by side
   - Place the heatmap below
   - Resize panels as needed

4. **Configure Dashboard Settings**
   - Click **Options** (gear icon)
   - Enable **Use margins between panels**
   - Enable **Show panel titles**

5. **Add Dashboard Filter** (Global)
   - Click **Add filter**
   - Field: `is_currently_assigned`, Operator: `is`, Value: `true`
   - Check **Pin across all apps** (optional)
   - This ensures only current assignments are shown

6. **Save Dashboard**
   - Click **Save**
   - **Title**: `Knowledge Application Report - Users by Project`
   - **Description**: `Shows number of users currently applying specific knowledge, grouped by projects`
   - Check **Store time with dashboard** if you want to save time range
   - Click **Save**

---

## Step 5: Using the Dashboard

### Interactive Filtering

1. **Filter by Knowledge**
   - Use the dropdown control at the top to select specific knowledge (e.g., "Java")
   - All visualizations will update automatically

2. **Filter by Territory**
   - Click **Add filter**
   - Field: `territory_name.keyword`, Operator: `is`, Value: `Colombia`
   - This answers queries like "Users applying Java in Colombia"

3. **Filter by Project Status**
   - Click **Add filter**
   - Field: `project_status.keyword`, Operator: `is`, Value: `Active`

### Example Business Questions You Can Answer

1. **"How many users are currently applying Java?"**
   - Use the filters control to select "Java"
   - Look at the bar chart total

2. **"Which projects are using Java, and how many users per project?"**
   - Filter by "Java" in the controls
   - Look at the data table for project breakdown

3. **"Give me names of employees applying Java in Active Projects in Colombia"**
   - Add filters: knowledge=Java, project_status=Active, territory=Colombia
   - Switch to **Discover** view with same filters
   - Add columns: `pragmatic_full_name`, `pragmatic_email`, `project_name`
   - Export results if needed

---

## Advanced: Create a Saved Search for Employee Names

If you frequently need to export employee names, create a saved search:

1. **Navigate to Discover**
   - Click hamburger menu → **Analytics** → **Discover**
   - Select `applied-knowledge-*` index pattern

2. **Add Filters**
   - `is_currently_assigned` = `true`
   - `project_status.keyword` = `Active`

3. **Add Columns**
   - Click **+** next to these fields:
     - `pragmatic_full_name`
     - `pragmatic_email`
     - `knowledge_name`
     - `project_name`
     - `territory_name`
     - `knowledge_level_name`

4. **Save Search**
   - Click **Save** at the top
   - Name: `Current Employee Assignments - Exportable`

5. **Add to Dashboard** (Optional)
   - Go back to your dashboard
   - Click **Add from library**
   - Select your saved search
   - This gives a detailed, exportable table

---

## Exporting Data

### Export from Discover
1. Apply your filters
2. Click **Share** → **CSV Reports** → **Generate CSV**
3. Download the CSV with employee names and details

### Export from Dashboard
1. Click **Share** → **PDF Reports** or **PNG**
2. Schedule periodic reports if needed (requires Kibana subscription)

---

## Sample Elasticsearch Query (for API Integration)

If you want to query this data programmatically from your Spring Boot application:

```json
POST http://localhost:9200/applied-knowledge-*/_search
Content-Type: application/json

{
  "size": 0,
  "query": {
    "bool": {
      "must": [
        { "term": { "is_currently_assigned": true } },
        { "term": { "project_status.keyword": "Active" } }
      ]
    }
  },
  "aggs": {
    "by_knowledge": {
      "terms": {
        "field": "knowledge_name.keyword",
        "size": 50
      },
      "aggs": {
        "by_project": {
          "terms": {
            "field": "project_name.keyword",
            "size": 100
          },
          "aggs": {
            "unique_users": {
              "cardinality": {
                "field": "pragmatic_id.keyword"
              }
            },
            "user_details": {
              "top_hits": {
                "size": 100,
                "_source": ["pragmatic_full_name", "pragmatic_email"],
                "sort": [{ "pragmatic_full_name.keyword": "asc" }]
              }
            }
          }
        }
      }
    }
  }
}
```

---

## Troubleshooting

### No Data in Kibana
```bash
# Check if Logstash is running and syncing
docker logs knowledge-tracker-logstash-dev --tail 50

# Verify data in Elasticsearch
curl "http://localhost:9200/applied-knowledge-*/_search?size=5&pretty"

# Check PostgreSQL has data
docker exec -it knowledge-tracker-postgres-dev psql -U pragma_dev -d knowledge_tracker_dev -c "SELECT COUNT(*) FROM applied_knowledge;"
```

### Fields Not Appearing as .keyword
- Go to **Stack Management** → **Index Patterns**
- Select `applied-knowledge-*`
- Click **Refresh field list** (refresh icon)

### Visualizations Not Updating
- Click the **Refresh** button in Kibana (top right)
- Ensure time range is set correctly (use "Last 30 days" or wider)

---

## Next Steps

1. **Add More Visualizations**:
   - Pie chart: Distribution by Territory
   - Line chart: Knowledge adoption over time
   - Metric: Total unique users currently assigned

2. **Create Multiple Dashboards**:
   - Executive Dashboard (high-level metrics)
   - Project Manager Dashboard (project-focused)
   - HR Dashboard (employee skills view)

3. **Set Up Alerts** (requires X-Pack/Elastic Stack features):
   - Alert when a critical knowledge has < N users
   - Alert when projects have no assigned users

4. **Integrate with Spring Boot**:
   - Create REST endpoints that query Elasticsearch
   - Return formatted results for business queries
   - Cache frequently requested reports

---

## Reference: Key Fields in applied-knowledge Index

| Field | Type | Description |
|-------|------|-------------|
| `pragmatic_id.keyword` | Keyword | Unique Pragmatic ID |
| `pragmatic_full_name.keyword` | Keyword | Employee full name |
| `pragmatic_email.keyword` | Keyword | Employee email |
| `knowledge_name.keyword` | Keyword | Knowledge name (Java, Python, etc.) |
| `knowledge_category_name.keyword` | Keyword | Category (Language, Framework, etc.) |
| `knowledge_level_name.keyword` | Keyword | Proficiency level |
| `project_name.keyword` | Keyword | Project name |
| `project_status.keyword` | Keyword | Active, Inactive, etc. |
| `account_name.keyword` | Keyword | Client account name |
| `territory_name.keyword` | Keyword | Geographic territory |
| `is_currently_assigned` | Boolean | Currently assigned (no offboard date) |
| `assignment_status.keyword` | Keyword | active, completed, inactive |
| `assignment_duration_days` | Number | Days assigned to project |

---

**Created**: 2025-11-07
**Version**: 1.0
**Last Updated**: 2025-11-07
